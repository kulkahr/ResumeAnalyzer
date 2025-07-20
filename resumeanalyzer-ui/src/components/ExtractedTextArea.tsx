import React from 'react';
import './ExtractedTextArea.css';
type Size = 'xs' | 'small' | 'medium' | 'large';
interface ExtractedTextAreaProps {
    id: string;
    label: string;
    value: string;
    size?: Size; // Optional size prop for textarea
}

const ExtractedTextArea: React.FC<ExtractedTextAreaProps> = ({ id, label, value, size }) => (
    <div className={size ? "extracted-text-area extracted-text-area-" + size : "extracted-text-area"}>
        <label htmlFor={id}>{label}</label>
        <textarea id={id} value={value} readOnly />
    </div>
);

export default ExtractedTextArea;
