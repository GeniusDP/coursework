package com.zaranik.coursework.checkerservice.dtos.container.response.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompilationReport {
  private CompilationStatus compilationStatus;
}
