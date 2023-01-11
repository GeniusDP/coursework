package com.zaranik.coursework.checkerservice.dtos.container.response;

import com.zaranik.coursework.checkerservice.dtos.container.response.checkstyle.CheckstyleReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.compilation.CompilationReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.pmd.PmdReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.unittesting.UnitTestingReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FullReport {
  private PmdReport pmdReport;
  private UnitTestingReport unitTestingReport;
  private CompilationReport compilationReport;
  private CheckstyleReport checkstyleReport;
}
