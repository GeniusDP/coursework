package com.example.solutionservice.dto.errors;

import lombok.Data;

@Data
public final class Violation {

    private final String fieldName;
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }


}