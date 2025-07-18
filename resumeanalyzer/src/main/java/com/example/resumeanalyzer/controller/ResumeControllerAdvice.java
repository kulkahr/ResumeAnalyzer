package com.example.resumeanalyzer.controller;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ResumeControllerAdvice {

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class
    })
    public ResponseEntity<String> handleValidationExceptions(Exception ex) {
        String filename = extractFilename(ex);
        if (filename != null) {
            log.warn("Validation failed for file '{}' at {}: {}", filename, Instant.now(), ex.getMessage());
        } else {
            log.warn("Validation failed at {}: {}", Instant.now(), ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file or parameters");
    }

    private String extractFilename(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) ex;
            return manve.getBindingResult().getFieldErrors().stream()
                    .filter(e -> e.getField().equals("file"))
                    .map(e -> {
                        Object rejected = e.getRejectedValue();
                        if (rejected instanceof MultipartFile) {
                            return ((MultipartFile) rejected).getOriginalFilename();
                        }
                        return null;
                    })
                    .findFirst().orElse(null);
        }
        // For ConstraintViolationException, filename is not directly available
        return null;
    }
}
