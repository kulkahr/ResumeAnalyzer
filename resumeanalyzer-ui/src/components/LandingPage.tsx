import React, { useState } from 'react';
import UploadForm from './UploadForm';
import ExtractedTextArea from './ExtractedTextArea';
import ErrorPopup from './ErrorPopup';
import './LandingPage.css';

const LandingPage: React.FC = () => {
    const [fileContent, setFileContent] = useState('');
    const [matchedSkills, setMatchedSkills] = useState<string[]>();
    const [missingSkills, setMissingSkills] = useState<string[]>();
    const [score, setScore] = useState<number>();
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        setError('');
        setFileContent('');
        const file = e.target.files?.[0];
        if (!file) return;
        const jobRole = (document.getElementById('jobRole') as HTMLInputElement).value.trim();
        if (!jobRole) return;
        // Validate file type
        const allowedTypes = [
            'application/pdf',
            'application/msword',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
            'text/plain'
        ];
        if (!allowedTypes.includes(file.type)) {
            setError('Please upload a valid resume file (PDF, DOC, DOCX, or TXT).');
            return;
        }

        if (file.size > 2 * 1024 * 1024) {
            setError('File size must be less than 2MB.');
            return;
        }
        setLoading(true);
        const formData = new FormData();
        formData.append('file', file);
        formData.append('jobRole', jobRole);
        try {
            const res = await fetch('/api/resume/upload', {
                method: 'POST',
                body: formData,
                headers: {
                    'Accept': 'application/json',
                },
                signal: AbortSignal.timeout(5000000),
            });
            const contentType = res.headers.get('content-type');
            if (res.ok && contentType && contentType.includes('application/json')) {
                const data = await res.json();
                if (typeof data.summary === 'string' && typeof data.matchedSkills === 'object' && Array.isArray(data.matchedSkills) && typeof data.missingSkills === 'object' && Array.isArray(data.missingSkills) && typeof data.score === 'number') {
                    setFileContent(data.summary);
                    setMatchedSkills(data.matchedSkills);
                    setMissingSkills(data.missingSkills);
                    setScore(data.score || []);
                } else {
                    setError('Unexpected response from server.');
                }
            } else if (res.ok) {
                // fallback for plain text
                const text = await res.text();
                setFileContent(text);
            } else {
                const data = await res.json();
                let errText = "";
                if (data && typeof data.error === 'string') {
                    errText = data.error;
                } else {
                    errText = await res.text();
                }
                setError(errText || 'Upload failed.');
            }
        } catch {
            setError('Network or server error.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="landing-page-container">
            <h1 className="landing-page-title">Resume Analyzer (Ai Powered)</h1>
            <div className="landing-page-card">
                <UploadForm loading={loading} onFileChange={handleFileChange} />
                {fileContent && <ExtractedTextArea id="suggestion" label="Suggestions" value={fileContent} />}
                {matchedSkills && <ExtractedTextArea id="matched-keywords" label="Keywords Found" value={matchedSkills.join(",")} size='medium' />}
                {missingSkills && <ExtractedTextArea id="missing-keywords" label="Keywords Missing" value={missingSkills.join(",")} size='medium' />}
                {score && <ExtractedTextArea id="score" label="Final Score" value={score.toString()} size='xs' />}
            </div>
            {error && <ErrorPopup message={error} />}
        </div>
    );
};

export default LandingPage;
