package org.example.project.dtos.unittesting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitTestingReport {
  private String message;
  private int testRun;
  private int testFailed;
}
