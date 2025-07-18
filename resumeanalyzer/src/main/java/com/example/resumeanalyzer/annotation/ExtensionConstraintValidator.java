package com.example.resumeanalyzer.annotation;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates that the file extension of a MultipartFile is one of the allowed extensions.
 */
@Slf4j
public class ExtensionConstraintValidator implements ConstraintValidator<ExtensionValidator, MultipartFile> {
    private String[] allowedExtensions;

    @Override
    public void initialize(ExtensionValidator constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.allowed();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Let @NotNull/@NotEmpty handle this
        }
        String filename = file.getOriginalFilename();
        if (filename == null) {
            log.warn("Validation failed: filename is null");
            return false;
        }
        String ext = filename.contains(".") ? filename.substring(filename.lastIndexOf('.') + 1).toLowerCase() : "";
        boolean valid = Arrays.stream(allowedExtensions)
                .map(String::toLowerCase)
                .anyMatch(ext::equals);
        if (!valid) {
            log.warn("Validation failed for file '{}' with extension '{}' at {}. Allowed: {}", filename, ext, java.time.Instant.now(), Arrays.toString(allowedExtensions));
        }
        return valid;
    }
}
