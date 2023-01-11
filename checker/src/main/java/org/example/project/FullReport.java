package org.example.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project.dtos.checkstyle.CheckstyleReport;
import org.example.project.dtos.compilation.CompilationReport;
import org.example.project.dtos.pmd.PmdReport;
import org.example.project.dtos.unittesting.UnitTestingReport;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullReport {
  private PmdReport pmdReport;
  private UnitTestingReport unitTestingReport;
  private CompilationReport compilationReport;
  private CheckstyleReport checkstyleReport;
}
