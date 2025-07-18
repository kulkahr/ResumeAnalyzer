package com.example.resumeanalyzer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ResumeService {
    public Path saveResume(MultipartFile file) throws IOException {
        Path tempDir = Files.createTempDirectory("resume_upload_");
        Path tempFile = tempDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }
}
