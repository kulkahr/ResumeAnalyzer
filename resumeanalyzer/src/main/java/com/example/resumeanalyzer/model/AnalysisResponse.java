package com.example.resumeanalyzer.model;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
public class AnalysisResponse {
    private String summary;
    private HashSet<String> missingSkills;
    private HashSet<String> matchedSkills;
    private int score;
}
