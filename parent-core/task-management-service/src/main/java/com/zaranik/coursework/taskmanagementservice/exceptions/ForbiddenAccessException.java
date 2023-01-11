package com.zaranik.coursework.taskmanagementservice.exceptions;

public class ForbiddenAccessException extends RuntimeException {

  public ForbiddenAccessException() {
    super();
  }

  public ForbiddenAccessException(String message) {
    super(message);
  }
}
