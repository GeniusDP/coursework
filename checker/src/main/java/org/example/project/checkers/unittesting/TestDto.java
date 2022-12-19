package org.example.project.checkers.unittesting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
class TestDto {
  private int testsRun;
  private int testsFailed;
}