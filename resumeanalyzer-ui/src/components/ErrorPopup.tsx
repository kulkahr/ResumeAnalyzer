import React from 'react';
import './ErrorPopup.css';

interface ErrorPopupProps {
    message: string;
}

const ErrorPopup: React.FC<ErrorPopupProps> = ({ message }) => (
    <div
        className="error-popup"
        role="alert"
        aria-live="assertive"
    >
        {message}
    </div>
);

export default ErrorPopup;
