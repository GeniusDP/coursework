package com.zaranik.coursework.checkerservice.exceptions;

public class ContainerTimeLimitExceededException extends RuntimeException {

  public ContainerTimeLimitExceededException() {
    super();
  }

  public ContainerTimeLimitExceededException(String message) {
    super(message);
  }
}
