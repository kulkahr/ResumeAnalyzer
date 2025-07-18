package com.example.resumeanalyzer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ResumeService {
    
    /**
     * Processes the uploaded resume file by saving it and extracting text.
     *
     * @param file the uploaded file
     * @return a JSONObject containing the extracted text
     * @throws IOException if an I/O error occurs
     */
    public JSONObject processResume(MultipartFile file) throws IOException {
        // Save the file and extract text
        Path savedFile = saveResume(file);
        return extractText(savedFile);
    }
    /**
     * Saves the uploaded resume file to a temporary directory.
     *
     * @param file the uploaded file
     * @return the path where the file is saved
     * @throws IOException if an I/O error occurs
     */
    public Path saveResume(MultipartFile file) throws IOException {
        Path tempDir = Files.createTempDirectory("resume_upload_");
        Path tempFile = tempDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        log.info("File '{}' saved to '{}'", file.getOriginalFilename(), tempFile);
        return tempFile;
    }

    /**
     * Extracts text from the given file using Apache Tika.
     *
     * @param filePath the path to the file
     * @return a JSONObject containing the extracted text
     * @throws IOException if an I/O error occurs or if the file does not exist
     */
    public JSONObject extractText(Path filePath) throws IOException {
        Tika tika = new Tika();
        if (!Files.exists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }
        try {
            String extractedData = tika.parseToString(filePath);
            log.trace("extracted data : {}" ,extractedData);
            log.info("Text extracted from file '{}' at {}", filePath.getFileName(), java.time.Instant.now());
            return new JSONObject().put("text", extractedData);
        } catch (TikaException e) {
            throw new IOException("Failed to extract text from file", e);
        }
    }
}
