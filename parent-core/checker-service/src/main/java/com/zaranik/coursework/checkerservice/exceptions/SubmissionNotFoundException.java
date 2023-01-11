package com.zaranik.coursework.checkerservice.exceptions;

public class SubmissionNotFoundException extends RuntimeException {

  public SubmissionNotFoundException() {
    super();
  }

  public SubmissionNotFoundException(String message) {
    super(message);
  }

  public SubmissionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SubmissionNotFoundException(Throwable cause) {
    super(cause);
  }
}
