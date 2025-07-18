package com.example.resumeanalyzer.controller;


import java.io.IOException;
import java.time.Instant;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resumeanalyzer.model.AnalysisRequest;
import com.example.resumeanalyzer.service.ResumeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@Valid @ModelAttribute AnalysisRequest request) {
        try {
            JSONObject savedFileData = resumeService.processResume(request);
            log.info("File '{}' uploaded and saved at {}", request.getFile().getOriginalFilename(), Instant.now());
            return ResponseEntity.ok(savedFileData.toString());
        } catch (IOException e) {
            log.error("Failed to save uploaded file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file");
        }
    }
}
