package com.example.resumeanalyzer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public enum DocumentField {
    NAME("name", "Name is missing"),
    EMAIL("email", "Email is not found"),
    PHONE("phone", "Personal phone number is not provided"),
    EXPERIENCE("experience", "Experience details are missing"),
    EDUCATION("education", "Education qualification is missing"),
    SKILLS("skills", "Skills are not listed");

    @NonNull
    private final String fieldName;
    @NonNull
    private final String fieldMissingMessage;

}
