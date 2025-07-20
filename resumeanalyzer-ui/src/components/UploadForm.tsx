import React, { useRef } from 'react';
import './UploadForm.css';

interface UploadFormProps {
    loading: boolean;
    onFileChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const UploadForm: React.FC<UploadFormProps> = ({ loading, onFileChange }) => {
    const fileInputRef = useRef<HTMLInputElement>(null);

    return (
        <form
            className="upload-form"
            onSubmit={e => e.preventDefault()}
        >
            <input
                id="jobRole"
                type="text"
                placeholder="Enter Job Role"
                className="job-role-input"
                required
            />
            <label htmlFor="file">
                Upload Resume (PDF or DOCX, max 2MB):
            </label>
            <input
                ref={fileInputRef}
                id="file"
                type="file"
                accept=".pdf,.docx"
                onChange={onFileChange}
            />
            <button
                type="button"
                onClick={() => fileInputRef.current?.click()}
                disabled={loading}
            >
                {loading ? 'Uploading...' : 'Upload Resume'}
            </button>
        </form>
    );
};

export default UploadForm;
