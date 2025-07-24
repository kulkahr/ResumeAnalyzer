package com.example.resumeanalyzer.exception.validations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.resumeanalyzer.exception.Document;
import com.example.resumeanalyzer.exception.DocumentField;
import com.example.resumeanalyzer.exception.DocumentValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumePersonNameValidation implements DocumentValidation {

    private final NameFinderME nameFinder;
    private static final String NAME_REGEX = "\\b([A-Z][a-zA-Z'-]+)\\s+([A-Z][a-zA-Z'-]+)\\b";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Override
    public void validate(Document document) throws DocumentValidationException, IOException {
        String resumeText = document.getDocumentText();
        DocumentValidationException nameException = new DocumentValidationException(DocumentField.NAME);
        if (resumeText.isBlank()) {
            throw nameException;
        }
        String[] lines = resumeText.split("\\r?\\n");
        String topSection = String.join(" ", Arrays.copyOfRange(lines, 0, Math.min(10, lines.length)));

        if (tryUsingNlpDetection(topSection)) {
            log.debug("NLP detection found spans further processing should be skipped: {}", document.getName());
            return;
        }
        if (tryRegexHeuristicDetection(topSection)) {
            log.debug("Regex heuristic detection succeeded further processing should be skipped: {}",
                    document.getName());
            return;
        }
        log.debug("No name detected in the top section of the resume: {}", document.getName());
        throw nameException;
    }

    private boolean tryUsingNlpDetection(String topSection) {

        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(topSection);
        Span[] spans = nameFinder.find(tokens);

        return !(spans == null || spans.length == 0);

    }

    private boolean tryRegexHeuristicDetection(String topSection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new StringReader(topSection))) {
            String line;

            while ((line = reader.readLine()) != null) {

                // Match two consecutive capitalized words (First Last)
                Matcher matcher = NAME_PATTERN.matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
        }
        return false;
    }

}
