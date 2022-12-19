package org.example.project.dtos.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CompilationReport {
  private CompilationStatus compilationStatus;
}
