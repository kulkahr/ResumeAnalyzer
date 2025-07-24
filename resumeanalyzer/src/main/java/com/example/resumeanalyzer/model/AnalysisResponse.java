package com.example.resumeanalyzer.model;

import java.util.HashSet;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Schema(description = "Result of resume analysis")
public class AnalysisResponse {
    @Schema(description = "Extracted summary from resume", example = "Experienced Java Developer with AI expertise")
    private String summary;
    @Schema(description = "List of missing skills based on job description", example = "[\"Docker\", \"Kubernetes\"]")
    private HashSet<String> missingSkills;
    @Schema(description = "List of matched skills", example = "[\"Java\", \"Spring Boot\"]")
    private HashSet<String> matchedSkills;
    @Schema(description = "Matching score out of total skills", example = "7")
    private int score;
    @Schema(description = "Any processing errors. Null if successfull")
    private String errors;
}
