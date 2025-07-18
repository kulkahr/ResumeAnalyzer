package com.example.resumeanalyzer.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.resumeanalyzer.service.ResumeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("Upload attempt with empty file at {}", Instant.now());
            return ResponseEntity.badRequest().body("No file uploaded");
        }
        try {
            Path savedFile = resumeService.saveResume(file);
            log.info("File '{}' uploaded and saved to '{}' at {}", file.getOriginalFilename(), savedFile, Instant.now());
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            log.error("Failed to save uploaded file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save file");
        }
    }
}
