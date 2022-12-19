package org.example.project.checkers.pmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
@AllArgsConstructor
public class Violation {
  private String value;
  private String beginLine;
  private String endLine;
  private String ruleName;
  private String packageName;
  private String className;
  private String methodName;

}
