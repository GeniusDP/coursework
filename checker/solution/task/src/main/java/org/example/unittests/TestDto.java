package org.example.unittests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class TestDto {
  private int testsRun;
  private int testsFailed;
}