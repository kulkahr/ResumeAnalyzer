package com.example.resumeanalyzer.annotation;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.PARAMETER;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom annotation to validate file extensions of uploaded files.
 * This annotation can be used on method parameters to ensure that the file
 * has one of the allowed extensions specified in the 'allowed' attribute.
 */
@Documented
@Constraint(validatedBy = {ExtensionConstraintValidator.class})
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface ExtensionValidator {
    String message() default "Invalid file extension";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] allowed() default {};
}
