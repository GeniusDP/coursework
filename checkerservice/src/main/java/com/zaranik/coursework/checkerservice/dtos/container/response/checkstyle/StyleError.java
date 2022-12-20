package com.zaranik.coursework.checkerservice.dtos.container.response.checkstyle;

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
public class StyleError {
  private String line;
  private String message;
  private String source;
}
