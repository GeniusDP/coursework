package com.zaranik.coursework.checkerservice.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ContainerRuntimeException.class)
  public String containerRuntimeExceptionHandler(){
    return "Runtime container error";
  }

}
