import React, { useState } from 'react';
import UploadForm from './UploadForm';
import ExtractedTextArea from './ExtractedTextArea';
import ErrorPopup from './ErrorPopup';
import './LandingPage.css';

const LandingPage: React.FC = () => {
    const [fileContent, setFileContent] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleFileChange = async (e: React.ChangeEvent<HTMLInputElement>) => {
        setError('');
        setFileContent('');
        const file = e.target.files?.[0];
        if (!file) return;
        if (file.size > 2 * 1024 * 1024) {
            setError('File size must be less than 2MB.');
            return;
        }
        setLoading(true);
        const formData = new FormData();
        formData.append('file', file);
        try {
            const res = await fetch('/api/resume/upload', {
                method: 'POST',
                body: formData,
                headers: {
                    'Accept': 'application/json',
                },
            });
            const contentType = res.headers.get('content-type');
            if (res.ok && contentType && contentType.includes('application/json')) {
                const data = await res.json();
                if (typeof data.text === 'string') {
                    setFileContent(data.text);
                } else {
                    setError('Unexpected response from server.');
                }
            } else if (res.ok) {
                // fallback for plain text
                const text = await res.text();
                setFileContent(text);
            } else {
                const errText = await res.text();
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
            <h1 className="landing-page-title">Resume Analyzer</h1>
            <div className="landing-page-card">
                <UploadForm loading={loading} onFileChange={handleFileChange} />
                {fileContent && <ExtractedTextArea value={fileContent} />}
            </div>
            {error && <ErrorPopup message={error} />}
        </div>
    );
};

export default LandingPage;
