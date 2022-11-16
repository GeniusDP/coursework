package com.example.demo.exceptions;

import com.example.demo.dto.AccessTokenInvalidException;
import com.example.demo.dto.errors.AppError;
import com.example.demo.dto.errors.ValidationExceptionResponse;
import com.example.demo.dto.errors.Violation;
import java.util.List;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(-1000)
public class GlobalExceptionHandler {


  @ExceptionHandler(AuthServiceUnreachableException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public AppError authServiceUnreachableExceptionHandler() {
    return AppError.justNow("Auth service unavailable");
  }

  @ExceptionHandler(AccessTokenInvalidException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public AppError accessTokenInvalidExceptionHandler() {
    return AppError.justNow("Access token is not valid");
  }

  @ExceptionHandler(ForbiddenAccessException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public AppError forbiddenAccessExceptionHandler() {
    return AppError.justNow("Access denied");
  }

  @ExceptionHandler(UnauthorizedException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public AppError unauthorizedExceptionHandler() {
    return AppError.justNow("Unauthorized");
  }

  @ExceptionHandler(UserNotActivatedException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public AppError userNotActivatedExceptionHandler() {
    return AppError.justNow("User is not activated yet");
  }

  @ExceptionHandler(UserDetailsUpdateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError userDetailsUpdateExceptionHandler() {
    return AppError.justNow("Cannot perform such update");
  }

  @ExceptionHandler(RegistrationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError registrationExceptionHandler() {
    return AppError.justNow("Registration failed");
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public AppError userNotFoundExceptionHandler() {
    return AppError.justNow("No such user found");
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationExceptionResponse argumentsNotValidExceptionHandle(BindException exception) {
    List<Violation> violations = exception.getAllErrors().stream()
        .map(objectError -> (FieldError) objectError)
        .map(objectError -> new Violation(objectError.getField(),
            objectError.getDefaultMessage()))
        .toList();
    return new ValidationExceptionResponse(violations);
  }

}
