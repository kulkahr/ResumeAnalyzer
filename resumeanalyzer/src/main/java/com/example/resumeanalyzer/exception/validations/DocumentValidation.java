package com.example.resumeanalyzer.exception.validations;

import java.io.IOException;

import com.example.resumeanalyzer.exception.Document;
import com.example.resumeanalyzer.exception.DocumentValidationException;

public interface DocumentValidation {

    void validate(Document document) throws DocumentValidationException, IOException;

}
