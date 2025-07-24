import React from 'react';
import './ValidationMessage.css';
type ValidationMessagesType = 'error' | 'success';
interface ValidationMessageProps {
    id: number
    message: string;
    type: ValidationMessagesType;

}

const ValidationMessage: React.FC<ValidationMessageProps> = ({ id, message, type }) => (
    <div id={`validation-message-${id}`} className="validation-message">

        <img
            src={type === 'error' ?
                "https://cdn-icons-png.flaticon.com/512/1828/1828665.png"
                :
                "https://cdn-icons-png.flaticon.com/512/845/845646.png"
            }
            alt={type}
            width="16"
            height="16"
            style={{ marginTop: "2px" }}
        />

        <span role="alert" aria-live="polite">{message}</span>
    </div>
);

export default ValidationMessage;