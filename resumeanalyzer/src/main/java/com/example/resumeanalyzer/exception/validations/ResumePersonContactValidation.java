package com.example.resumeanalyzer.exception.validations;

import com.example.resumeanalyzer.exception.Document;
import com.example.resumeanalyzer.exception.DocumentField;
import com.example.resumeanalyzer.exception.DocumentValidationException;
import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.Iterator;
import jakarta.validation.constraints.NotNull;

public class ResumePersonContactValidation implements DocumentValidation {
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public void validate(@NotNull Document document) throws DocumentValidationException {
        Iterable<PhoneNumberMatch> numbers = phoneUtil.findNumbers(document.getDocumentText(), null); // null = auto

        Iterator<PhoneNumberMatch> iterator = numbers.iterator();

        if (!iterator.hasNext()) {
            throw new DocumentValidationException(DocumentField.PHONE);
        }

    }

}
