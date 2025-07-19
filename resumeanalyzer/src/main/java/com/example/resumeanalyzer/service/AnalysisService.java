package com.example.resumeanalyzer.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import com.example.resumeanalyzer.model.AnalysisResponse;
import com.example.resumeanalyzer.model.AnalysisResponse.AnalysisResponseBuilder;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {
    
    private final HashMap<String, HashSet<String>> skillSets = new HashMap<>();

    private static final String RESUME_ANALYSIS_PROMPT = "Analyze the following resume and suggest improvements for a %s role %s";

    private static final String JOB_ROLE_SETUP_PROMPT = "List the top 20 most in-demand job roles in the technology and software industry in 2025. Respond in a single line, comma-separated format only, all in lowercase. Do not include numbers, bullet points, or any explanations.";

    private static final String SKILL_SET_PROMPT = "List the top 20 skills required for a %s in the technology industry. Respond in a single line, comma-separated format only, all in lowercase. Do not include numbers, bullet points, or any explanations.";

    private final AiService aiService;

    /**
     * Initializes the service with predefined skill sets for different job roles.
     * This method is called after the service is constructed to populate the skill sets.
     */
    @PostConstruct
    public void init() {
        // Initialize skill sets for different job roles
        Arrays.asList(aiService.ask(JOB_ROLE_SETUP_PROMPT).split(",")).forEach(role-> {
            skillSets.put(role.trim(), new HashSet<>(Arrays.asList(aiService.ask(SKILL_SET_PROMPT.formatted(role.trim())).split(","))));
        });
        if(!skillSets.containsKey("software engineer")) {
            skillSets.put("software engineer", new HashSet<>(Arrays.asList("java", "spring", "microservices")));
        }
        if(!skillSets.containsKey("data scientist")) {
            skillSets.put("data scientist", new HashSet<>(Arrays.asList("python", "machine learning", "data analysis")));
        }
        if(!skillSets.containsKey("devops engineer")) {
            skillSets.put("devops engineer", new HashSet<>(Arrays.asList("docker", "kubernetes", "ci/cd")));
        }
        // Add more roles and their skills as needed
    }

    /**
     * Analyzes the resume text and returns an AnalysisResponse containing the skills found.
     * 
     * @param resumeText The text content of the resume to analyze.
     * @return AnalysisResponse containing the skills found in the resume.
     */
    public AnalysisResponse analyze(String resumeText, String jobRole) {
        String summary = aiService.ask(RESUME_ANALYSIS_PROMPT.formatted(
            jobRole, resumeText
        ));
        return analyzeSkillsInResume(resumeText, getSkillsForJobRole(jobRole)).summary(summary)
                .build();
    }


    /**
     * Analyzes the resume text against the required skills for a specific job role.
     * 
     * @param resumeText The text content of the resume to analyze.
     * @param requiredSkillSet The set of skills required for the job role.
     * @return AnalysisResponse containing matched and missing skills along with a score.
     */
    private AnalysisResponseBuilder analyzeSkillsInResume(String resumeText, HashSet<String> requiredSkillSet) {
        HashSet<String> matchedSkills = new HashSet<>();
        HashSet<String> missingSkills = new HashSet<>(requiredSkillSet);
        if (resumeText == null || resumeText.isEmpty()) {
            log.warn("Resume text is empty or null.");
            return AnalysisResponse.builder()
                    .matchedSkills(matchedSkills)
                    .missingSkills(missingSkills)
                    .score(0);
        }
        resumeText = resumeText.toLowerCase();
        // Simulate skill matching logic
        for (String skill : requiredSkillSet) {
            if (resumeText.contains(skill)) {
                matchedSkills.add(skill);
                missingSkills.remove(skill);
            }
        }

        int score = (int) ((matchedSkills.size() / (double) requiredSkillSet.size()) * 100);
        
        return AnalysisResponse.builder()
                .matchedSkills(matchedSkills)
                .missingSkills(missingSkills)
                .score(score);
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
