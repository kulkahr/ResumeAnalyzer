package com.example.resumeanalyzer.exception;

import java.nio.file.Path;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class Document {
    @NonNull
    private String name;
    @NonNull
    private String documentText;
    private Path documentLocation;

}
