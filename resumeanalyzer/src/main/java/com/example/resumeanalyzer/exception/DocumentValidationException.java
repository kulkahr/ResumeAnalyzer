package com.example.resumeanalyzer.exception;

public class DocumentValidationException extends RuntimeException {
    private final DocumentField field;

    public DocumentValidationException(DocumentField field) {
        super(field.getFieldMissingMessage());
        this.field = field;
    }

    public DocumentField getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return field.getFieldMissingMessage();
    }
}
