package com.example.resumeanalyzer.model;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisResponse {
    private String summary;
    private HashSet<String> missingSkills;
    private HashSet<String> matchedSkills;
    private int score;
}
