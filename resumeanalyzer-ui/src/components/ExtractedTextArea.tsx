import React from 'react';
import './ExtractedTextArea.css';

interface ExtractedTextAreaProps {
    value: string;
}

const ExtractedTextArea: React.FC<ExtractedTextAreaProps> = ({ value }) => (
    <div className="extracted-text-area">
        <label>Extracted Content:</label>
        <textarea value={value} readOnly />
    </div>
);

export default ExtractedTextArea;
