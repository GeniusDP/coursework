package com.example.demo.dto.errors;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public final class Violation {

    @Schema(description = "field name")
    private final String fieldName;

    @Schema(description = "message:")
    private final String message;

    public Violation(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }


}