package com.example.resumeanalyzer.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.example.resumeanalyzer.model.AnalysisResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AnalysisService {
    
    private final HashMap<String, HashSet<String>> skillSets = new HashMap<>();

    /**
     * Initializes the service with predefined skill sets for different job roles.
     * This method is called after the service is constructed to populate the skill sets.
     */
    @PostConstruct
    public void init() {
        // Initialize skill sets for different job roles
        skillSets.put("Software Engineer", new HashSet<>(Arrays.asList("Java", "Spring", "Microservices")));
        skillSets.put("Data Scientist", new HashSet<>(Arrays.asList("Python", "Machine Learning", "Data Analysis")));
        skillSets.put("DevOps Engineer", new HashSet<>(Arrays.asList("Docker", "Kubernetes", "CI/CD")));
        // Add more roles and their skills as needed
    }

    /**
     * Analyzes the resume text and returns an AnalysisResponse containing the skills found.
     * 
     * @param resumeText The text content of the resume to analyze.
     * @return AnalysisResponse containing the skills found in the resume.
     */
    public AnalysisResponse analyze(String resumeText, String jobRole) {
        return analyzeSkillsInResume(resumeText, getSkillsForJobRole(jobRole));
    }


    /**
     * Analyzes the resume text against the required skills for a specific job role.
     * 
     * @param resumeText The text content of the resume to analyze.
     * @param requiredSkillSet The set of skills required for the job role.
     * @return AnalysisResponse containing matched and missing skills along with a score.
     */
    private AnalysisResponse analyzeSkillsInResume(String resumeText, HashSet<String> requiredSkillSet) {
        AnalysisResponse response = new AnalysisResponse();
        HashSet<String> matchedSkills = new HashSet<>();
        HashSet<String> missingSkills = new HashSet<>(requiredSkillSet);

        // Simulate skill matching logic
        for (String skill : requiredSkillSet) {
            if (resumeText.contains(skill)) {
                matchedSkills.add(skill);
                missingSkills.remove(skill);
            }
        }

        int score = (int) ((matchedSkills.size() / (double) requiredSkillSet.size()) * 100);
        
        response.setMatchedSkills(matchedSkills);
        response.setMissingSkills(missingSkills);
        response.setScore(score);
        
        return response;
    }

    /**
     * Retrieves the skills associated with a specific job role.
     * 
     * @param jobRole The job role for which to retrieve skills.
     * @return A set of skills associated with the specified job role.
     */
    private HashSet<String> getSkillsForJobRole(String jobRole) {
        return skillSets.getOrDefault(jobRole, new HashSet<>());
    }
}
