package com.example.resumeanalyzer.model;

import java.util.HashSet;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AnalysisResponse {
    private String summary;
    private HashSet<String> missingSkills;
    private HashSet<String> matchedSkills;
    private int score;
    private List<String> validationErrors;
    private String errors;
}
