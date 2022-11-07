package com.zaranik.cursework.authservice.exceptions;

import com.zaranik.cursework.authservice.dto.errors.ValidationExceptionResponse;
import com.zaranik.cursework.authservice.dto.errors.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessTokenInvalidException.class)
    public ResponseEntity<String> accessTokenInvalidExceptionHandler() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ResponseEntity<String> refreshTokenExpiredExceptionHandler() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> loginExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> registrationExceptionHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionResponse methodArgumentNotValidExceptionHandle(BindException exception) {
        List<Violation> violations = exception.getAllErrors().stream().map(objectError -> {
            return new Violation(((FieldError) objectError).getField(), objectError.getDefaultMessage());
        }).toList();
        return new ValidationExceptionResponse(violations);
    }

}
