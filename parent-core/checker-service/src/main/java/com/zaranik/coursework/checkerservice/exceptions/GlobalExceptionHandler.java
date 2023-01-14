package com.zaranik.coursework.checkerservice.exceptions;

import com.zaranik.coursework.checkerservice.dtos.response.RateLimitDto;
import com.zaranik.coursework.checkerservice.exceptions.errors.AppError;
import com.zaranik.coursework.checkerservice.exceptions.errors.ValidationExceptionResponse;
import com.zaranik.coursework.checkerservice.exceptions.errors.Violation;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
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

  @ExceptionHandler(ContainerRuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError containerRuntimeExceptionHandler() {
    return AppError.justNow("Container failed in runtime");
  }

  @ExceptionHandler(SizeLimitExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError sizeLimitExceededExceptionHandler() {
    return AppError.justNow("Request size limit exceeded");
  }

  @ExceptionHandler(TooManyRequestsException.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  public RateLimitDto tooManyRequestsExceptionHandler(TooManyRequestsException e) {
    return e.getRateLimitDto();
  }

  @ExceptionHandler(ContainerTimeLimitExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError containerTimeLimitExceededExceptionHandler() {
    return AppError.justNow("Time limit for container exceeded");
  }

  @ExceptionHandler(SubmissionNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public AppError submissionNotFoundExceptionHandler() {
    return AppError.justNow("Submission not found");
  }

  @ExceptionHandler(NoSubmissionsLeftException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AppError noSubmissionsLeftException() {
    return AppError.justNow("no submissions left for this task, you have reached the limit");
  }

  @ExceptionHandler(TaskNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public AppError taskNotFoundExceptionHandler() {
    return AppError.justNow("Task not found");
  }

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
