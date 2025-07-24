package com.example.resumeanalyzer.exception.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.resumeanalyzer.exception.Document;
import com.example.resumeanalyzer.exception.DocumentField;
import com.example.resumeanalyzer.exception.DocumentValidationException;

@Component
public class ResumePersonEmailValidation implements DocumentValidation {
    private static final String EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void validate(Document document) throws DocumentValidationException {
        String resumeText = document.getDocumentText();
        DocumentValidationException emailException = new DocumentValidationException(DocumentField.EMAIL);
        if (resumeText.isBlank()) {
            throw emailException;
        }

        // Best practice email regex (RFC 5322 Official Standard compatible for most
        // real-world use cases)

        Matcher matcher = EMAIL_PATTERN.matcher(resumeText);

        if (!matcher.find()) {
            throw emailException;
        }
    }

}
