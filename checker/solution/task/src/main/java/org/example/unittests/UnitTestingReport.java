package org.example.unittests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitTestingReport {
  private String message;
  private int testPassed;
  private int testFailed;
}
