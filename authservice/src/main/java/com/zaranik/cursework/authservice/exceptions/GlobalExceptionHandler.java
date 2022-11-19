package com.zaranik.cursework.authservice.exceptions;

import com.zaranik.cursework.authservice.dto.errors.AppError;
import com.zaranik.cursework.authservice.dto.errors.ValidationExceptionResponse;
import com.zaranik.cursework.authservice.dto.errors.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessTokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AppError accessTokenInvalidExceptionHandler() {
        return AppError.justNow("Access token is not valid");
    }

    @ExceptionHandler(UserNotActivatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError userNotActivatedExceptionHandler() {
        return AppError.justNow("User is not activated yet");
    }

    @ExceptionHandler(RefreshTokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AppError refreshTokenExpiredExceptionHandler() {
        return AppError.justNow("Refresh token is not valid: tampered or expired");
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError loginExceptionHandler() {
        return AppError.justNow("Wrong login or password");
    }

    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AppError registrationExceptionHandler() {
        return AppError.justNow("Registration failed");
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
