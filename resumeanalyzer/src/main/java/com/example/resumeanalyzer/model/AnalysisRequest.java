package com.example.resumeanalyzer.model;

import org.springframework.web.multipart.MultipartFile;

import com.example.resumeanalyzer.annotation.ExtensionValidator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisRequest {

    @NotEmpty
    @Schema(description = "Job role for analysis", example = "Java Developer")
    private String jobRole;

    @NotNull
    @ExtensionValidator(allowed = { "pdf", "docx" })
    @Schema(description = "File (PDF or DOCX)", type = "string", format = "binary")
    private MultipartFile file;
}
