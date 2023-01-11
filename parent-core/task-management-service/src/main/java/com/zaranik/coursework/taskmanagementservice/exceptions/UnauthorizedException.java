package com.zaranik.coursework.taskmanagementservice.exceptions;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
    super();
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
