package com.example.resumeanalyzer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.resumeanalyzer.exception.Document;
import com.example.resumeanalyzer.exception.DocumentValidationException;
import com.example.resumeanalyzer.exception.validations.DocumentValidation;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationProcessorService {
    private final List<DocumentValidation> validators;

    @PostConstruct
    public void init() {
        log.info("Registered {} custom validation beans", validators.size());
    }

    public List<DocumentValidationException> validate(@NotNull Document document) {
        List<DocumentValidationException> exceptions = new ArrayList<>();

        for (DocumentValidation validator : validators) {
            try {
                validator.validate(document);
            } catch (DocumentValidationException e) {
                exceptions.add(e);
                log.warn("Validation failed: {} - {}", e.getField(), e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error during validation in {}: {}", validator.getClass().getSimpleName(),
                        e.getMessage(), e);
            }
        }

        return exceptions;
    }
}
