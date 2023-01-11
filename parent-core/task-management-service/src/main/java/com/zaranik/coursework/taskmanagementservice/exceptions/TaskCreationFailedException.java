package com.zaranik.coursework.taskmanagementservice.exceptions;

public class TaskCreationFailedException extends RuntimeException {

  public TaskCreationFailedException() {
    super();
  }

  public TaskCreationFailedException(String message) {
    super(message);
  }

  public TaskCreationFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public TaskCreationFailedException(Throwable cause) {
    super(cause);
  }
}
