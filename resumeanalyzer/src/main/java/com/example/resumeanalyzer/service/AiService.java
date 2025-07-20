package com.example.resumeanalyzer.service;

import java.util.List;

import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class AiService {

    private final OllamaApi chatClient;

    public String ask(String prompt) {
        List<OllamaApi.Message> messages = List.of(
                OllamaApi.Message.builder(OllamaApi.Message.Role.USER).content(prompt).build());
        OllamaApi.ChatRequest chatRequest = OllamaApi.ChatRequest.builder("llama3").messages(messages).build();
        log.info("Sending prompt to AI: {}", prompt);
        return chatClient.chat(chatRequest).message().content();
    }
}
