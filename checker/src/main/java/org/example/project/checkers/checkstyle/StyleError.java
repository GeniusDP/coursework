package org.example.project.checkers.checkstyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StyleError {
  private String line;
  private String message;
  private String source;
}
