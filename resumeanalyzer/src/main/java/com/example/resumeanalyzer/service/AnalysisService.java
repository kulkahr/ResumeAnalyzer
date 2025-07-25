package com.example.resumeanalyzer.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    private final ConcurrentHashMap<String, HashSet<String>> skillSets = new ConcurrentHashMap<>();

    private static final String RESUME_ANALYSIS_PROMPT = "Analyze the following resume and suggest improvements for a %s role. If analysis takes too long, stop early and return whatever suggestions are ready in 30 seconds. Respond only the improvments. Resume text: %s";

    private static final String JOB_ROLE_SETUP_PROMPT = "List the top 5 most in-demand job roles in the technology and software industry in 2025. Respond in a single line, comma-separated format only, all in lowercase. Do not include numbers, bullet points, or any explanations.";

    private static final String SKILL_SET_PROMPT = "List the 10 top skills and key technical keywords required for the job role of \"%s\". Respond only with comma-separated lowercase words, no explanations, no bullet points.";

    private final AiService aiService;

    /**
     * Initializes the service with predefined skill sets for different job roles.
     * This method is called after the service is constructed to populate the skill
     * sets.
     */
    @PostConstruct
    public void init() {
        log.info("Initializing skill sets for job roles...");
        // Initialize skill sets for different job roles
        try {
            Arrays.asList(aiService.ask(JOB_ROLE_SETUP_PROMPT).split(","))
                    .stream()
                    .filter(Objects::isNull)
                    .map(String::trim)
                    .forEach(role -> {
                        try {
                            skillSets.put(role,
                                    computeSkills(aiService.ask(SKILL_SET_PROMPT.formatted(role))));
                        } catch (Exception e) {
                            log.error("Failed to initialize skills for role '{}': {}", role, e.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to initialize job roles and skills: {}", e.getMessage());
        }
        if (!skillSets.containsKey("software engineer")) {
            skillSets.put("software engineer", computeSkills("java,spring,microservices"));
        }
        if (!skillSets.containsKey("data scientist")) {
            skillSets.put("data scientist",
                    computeSkills("python,machine learning,data analysis"));
        }
        if (!skillSets.containsKey("devops engineer")) {
            skillSets.put("devops engineer", computeSkills("docker,kubernetes,ci/cd"));
        }
        log.info("Initialized skill sets for job roles: {}", skillSets);
        // Add more roles and their skills as needed
    }

    /**
     * Analyzes the resume text and returns an AnalysisResponse containing the
     * skills found.
     * 
     * @param resumeText The text content of the resume to analyze.
     * @return AnalysisResponse containing the skills found in the resume.
     */
    public AnalysisResponse analyze(String resumeText, String jobRole) {
        String summary = aiService.ask(RESUME_ANALYSIS_PROMPT.formatted(
                jobRole, resumeText));
        return analyzeSkillsInResume(resumeText, getSkillsForJobRole(jobRole.toLowerCase())).summary(summary)
                .build();
    }

    /**
     * Analyzes the resume text against the required skills for a specific job role.
     * 
     * @param resumeText       The text content of the resume to analyze.
     * @param requiredSkillSet The set of skills required for the job role.
     * @return AnalysisResponse containing matched and missing skills along with a
     *         score.
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
        Map<String, Pattern> skillPatterns = requiredSkillSet.stream()
                .collect(Collectors.toMap(
                        skill -> skill,
                        skill -> Pattern.compile("\\b" + Pattern.quote(skill) + "\\b", Pattern.CASE_INSENSITIVE)));
        // Simulate skill matching logic
        for (Map.Entry<String, Pattern> entry : skillPatterns.entrySet()) {
            if (entry.getValue().matcher(resumeText).find()) {
                String skill = entry.getKey();
                matchedSkills.add(skill);
                missingSkills.remove(skill);
            }
        }

        int score = requiredSkillSet.isEmpty() ? 0
                : (int) ((matchedSkills.size() / (double) requiredSkillSet.size()) * 100);

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
        skillSets.computeIfAbsent(jobRole,
                role -> computeSkills(
                        aiService.ask(SKILL_SET_PROMPT.formatted(role))));
        return skillSets.getOrDefault(jobRole, new HashSet<>());
    }

    /**
     * Computes a set of skills from a comma-separated string.
     * 
     * @param skill A comma-separated string of skills.
     * @return A HashSet containing the individual skills.
     */
    private HashSet<String> computeSkills(String skill) {
        HashSet<String> skillSet = new HashSet<>();
        Arrays.stream(skill.split(","))
                .map(String::trim)
                .map(skillStr -> skillStr.replaceAll("[\\(\\)\\[\\]{},.-]", ""))
                .filter(s -> !s.isEmpty())
                .forEach(skillSet::add);
        return skillSet;
    }
}
