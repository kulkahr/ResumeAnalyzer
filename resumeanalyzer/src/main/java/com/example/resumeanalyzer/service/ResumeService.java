package com.example.resumeanalyzer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.resumeanalyzer.exception.Document;
import com.example.resumeanalyzer.model.AnalysisRequest;
import com.example.resumeanalyzer.model.AnalysisResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResumeService {

    private static final Tika tika = new Tika();

    private final AnalysisService analysisService;

    /**
     * Processes the uploaded resume file by saving it and extracting text.
     *
     * @param request the analysis request containing the uploaded file
     * @return a AnalysisResponse containing the analyzed skills and summary
     * @throws IOException              if an I/O error occurs
     * @throws IllegalArgumentException if no file is uploaded
     */
    public AnalysisResponse processResume(AnalysisRequest request) throws IOException {
        // Save the file and extract text
        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new IllegalArgumentException("No file uploaded");
        }
        log.info("Processing resume file: {}", request.getFile().getOriginalFilename());
        Path savedFile = saveResume(request.getFile());
        Document document = new Document(
                request.getFile().getOriginalFilename(),
                extractText(savedFile));
        return analysisService.analyze(document, request.getJobRole());
    }

    /**
     * Saves the uploaded resume file to a temporary directory.
     *
     * @param file the uploaded file
     * @return the path where the file is saved
     * @throws IOException if an I/O error occurs
     */
    public Path saveResume(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        if (file.getSize() > 10_000_000) { // 10MB limit
            throw new IllegalArgumentException("File size exceeds limit");
        }

        // Sanitize filename to prevent path traversal
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        String sanitizedFilename = Paths.get(originalFilename).getFileName().toString();
        Path tempDir = Files.createTempDirectory("resume_upload_");
        Path tempFile = tempDir.resolve(sanitizedFilename);
        Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        log.info("File '{}' saved to '{}'", sanitizedFilename, tempFile);
        return tempFile;
    }

    /**
     * Extracts text from the given file using Apache Tika.
     *
     * @param filePath the path to the file
     * @return a String containing the extracted text
     * @throws IOException if an I/O error occurs or if the file does not exist
     */
    public String extractText(Path filePath) throws IOException {
        if (filePath == null || filePath.toString().isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        log.info("Extracting text from file: {}", filePath.getFileName());
        if (!Files.exists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }
        try {
            String extractedData = tika.parseToString(filePath);
            log.info("Text extracted from file '{}' at {}", filePath.getFileName(), java.time.Instant.now());
            return extractedData;
        } catch (TikaException e) {
            throw new IOException("Failed to extract text from file", e);
        }
    }
}
