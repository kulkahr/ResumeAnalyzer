package com.example.resumeanalyzer.model;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Getter
@Setter
@Builder
@ToString
public class AnalysisResponse {
    private String summary;
    private HashSet<String> missingSkills;
    private HashSet<String> matchedSkills;
    private int score;
    private String errors;
}
