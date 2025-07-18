import React from 'react';
import './ErrorPopup.css';

interface ErrorPopupProps {
    message: string;
}

const ErrorPopup: React.FC<ErrorPopupProps> = ({ message }) => (
    <div className="error-popup">
        {message}
    </div>
);

export default ErrorPopup;
