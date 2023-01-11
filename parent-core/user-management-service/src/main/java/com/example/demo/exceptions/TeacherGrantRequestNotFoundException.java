package com.example.demo.exceptions;

public class TeacherGrantRequestNotFoundException extends RuntimeException {

  public TeacherGrantRequestNotFoundException() {
    super();
  }

  public TeacherGrantRequestNotFoundException(String message) {
    super(message);
  }

  public TeacherGrantRequestNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public TeacherGrantRequestNotFoundException(Throwable cause) {
    super(cause);
  }

  protected TeacherGrantRequestNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
