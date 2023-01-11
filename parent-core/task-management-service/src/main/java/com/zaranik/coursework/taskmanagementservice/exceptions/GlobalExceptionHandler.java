package com.zaranik.coursework.taskmanagementservice.exceptions;

import com.zaranik.coursework.taskmanagementservice.dto.errors.AppError;
import com.zaranik.coursework.taskmanagementservice.dto.errors.ValidationExceptionResponse;
import com.zaranik.coursework.taskmanagementservice.dto.errors.Violation;
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
    return AppError.justNow("Service unavailable");
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

  @ExceptionHandler(TaskCreationFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError taskCreationFailedExceptionHandler() {
    return AppError.justNow("creation of task failed due to validation or archive content problem");
  }

  @ExceptionHandler(TaskNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public AppError taskNotFoundExceptionHandler() {
    return AppError.justNow("no such task found");
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
