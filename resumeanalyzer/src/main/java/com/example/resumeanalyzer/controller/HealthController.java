package com.example.resumeanalyzer.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health API", description = "Operations health check for the application")
public class HealthController {
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/api/health")
    @Operation(summary = "Health Check", description = "Returns a basic health status for the app")
    @ApiResponse(responseCode = "200", description = "Application is running", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(example = "{ \"status\": \"UP\", \"timestamp\": \"2023-10-01T12:00:00Z\" }")))
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        String timestamp = Instant.now().toString();
        response.put("timestamp", timestamp);
        logger.info("Health check requested at {}", timestamp);
        return response;
    }
}
