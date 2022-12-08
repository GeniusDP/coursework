package com.zaranik.coursework.checkerservice.exceptions.errors;

import lombok.Data;

import java.util.List;

@Data
public class ValidationExceptionResponse {

    private List<Violation> violations;

    public ValidationExceptionResponse(List<Violation> violations) {
        this.violations = violations;
    }

}