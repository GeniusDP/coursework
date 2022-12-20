package com.zaranik.coursework.checkerservice.dtos.container.response.unittesting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UnitTestingReport {
  private String message;
  private int testRun;
  private int testFailed;
}
