package com.zaranik.coursework.checkerservice.dtos;

public record CheckingReport(
  Long solutionId,
  String compilationStatus,
  Integer testsNumber,
  Integer testsPassed,
  String testingStatus) {

}
