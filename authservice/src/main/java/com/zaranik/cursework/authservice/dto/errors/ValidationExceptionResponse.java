package com.zaranik.cursework.authservice.dto.errors;

import lombok.Data;

import java.util.List;

@Data
public class ValidationExceptionResponse {

    private List<Violation> violations;

    public ValidationExceptionResponse(List<Violation> violations) {
        this.violations = violations;
    }

}