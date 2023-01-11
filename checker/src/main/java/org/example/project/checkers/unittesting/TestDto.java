package org.example.project.checkers.unittesting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class TestDto {
  private int testsRun;
  private int testsFailed;
}