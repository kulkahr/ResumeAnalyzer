package com.example.resumeanalyzer.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI resumeAnalyzerOpenAPI(
            @Value("${config.version:v1.0}") String version,
            @Value("${config.developer.name:Not Provided}") String developer,
            @Value("${config.developer.email:Not Provided}") String developerEmail) {
        return new OpenAPI()
                .info(new Info()
                        .title("Resume Analyzer API")
                        .version(version)
                        .description("API for analyzing resumes against job descriptions using AI.")
                        .contact(new Contact().name(developer).email(developerEmail)));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("resume-analyzer")
                .pathsToMatch("/api/**")
                .build();
    }
}
