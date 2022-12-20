package com.zaranik.coursework.checkerservice.dtos.container.response.pmd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Violation {
  private String value;
  private Integer beginLine;
  private Integer endLine;
  private String ruleName;
  private String packageName;
  private String className;
  private String methodName;

}
