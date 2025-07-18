package com.example.resumeanalyzer.model;

import org.springframework.web.multipart.MultipartFile;

import com.example.resumeanalyzer.annotation.ExtensionValidator;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisRequest {
    private @NotEmpty String jobRole;
    private  @NotNull @ExtensionValidator(allowed={"pdf","docx"}) MultipartFile file;
}
