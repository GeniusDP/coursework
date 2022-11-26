package com.example.demo.entities;

public enum RoleValue {
  STUDENT,
  TEACHER,
  ADMIN;

  public static RoleValue getRoleValue(String name) {
    if (name == null) {
      return null;
    }
    return switch (name) {
      case "STUDENT" -> STUDENT;
      case "TEACHER" -> TEACHER;
      case "ADMIN" -> ADMIN;
      default -> null;
    };
  }

}
