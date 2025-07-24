package com.example.resumeanalyzer.controller;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resumeanalyzer.model.AnalysisRequest;
import com.example.resumeanalyzer.model.AnalysisResponse;
import com.example.resumeanalyzer.service.ResumeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resume")
@Tag(name = "Resume API", description = "Operations related to resume uploads and analysis")
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload a resume", description = "Parses and analyzes resume content using AI.", requestBody = @RequestBody(content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = AnalysisRequest.class))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalysisResponse.class), examples = @ExampleObject(value = "{ \"summary\": \"Senior Dev\", \"missingSkills\": [\"Docker\"], \"matchedSkills\": [\"Java\"], \"score\": 8, \"validationErrors\": [], \"errors\": null }"))),
            @ApiResponse(responseCode = "400", description = "Invalid input or missing file", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalysisResponse.class), examples = @ExampleObject(value = "{ \"errors\": \"Error Message\" }"))),
            @ApiResponse(responseCode = "500", description = "Internal server error during AI processing", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnalysisResponse.class), examples = @ExampleObject(value = "{ \"errors\": \"Error Message\" }")))
    })
    public ResponseEntity<AnalysisResponse> uploadResume(@Valid @ModelAttribute AnalysisRequest request) {
        try {
            AnalysisResponse response = resumeService.processResume(request);
            log.info("File '{}' uploaded and saved at {}", request.getFile().getOriginalFilename(), Instant.now());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Failed to save uploaded file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AnalysisResponse.builder().errors("Failed to save file").build());
        } catch (Exception e) {
            log.error("Unexpected error during file upload: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AnalysisResponse.builder().errors("Unexpected error occurred").build());
        }
    }
}
