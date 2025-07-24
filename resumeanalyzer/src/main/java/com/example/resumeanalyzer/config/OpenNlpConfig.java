package com.example.resumeanalyzer.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;

@Configuration
public class OpenNlpConfig {

    @Bean
    public TokenNameFinderModel personModel(
            @Value("${nlp.person-model:nlp_models/en-ner-person.bin}") String modelPath) {
        try (InputStream modelStream = new ClassPathResource(modelPath).getInputStream()) {
            return new TokenNameFinderModel(modelStream);
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load OpenNLP person model from " + modelPath, ex);
        }
    }

    @Bean
    public NameFinderME nameFinderME(TokenNameFinderModel personModel) {
        return new NameFinderME(personModel); // NameFinderME is thread-safe
    }

}
