package com.example.solutionservice.exceptions;

import com.example.solutionservice.dto.errors.AppError;
import com.example.solutionservice.dto.errors.ValidationExceptionResponse;
import com.example.solutionservice.dto.errors.Violation;
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

  @ExceptionHandler(SolutionNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public AppError solutionNotFoundExceptionHandler(){
    return AppError.justNow("no such solution found");
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
