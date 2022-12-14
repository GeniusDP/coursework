package com.zaranik.coursework.taskmanagementservice.exceptions;

public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException() {
    super();
  }

  public TaskNotFoundException(String message) {
    super(message);
  }

  public TaskNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskNotFoundException(Throwable cause) {
    super(cause);
  }
}
