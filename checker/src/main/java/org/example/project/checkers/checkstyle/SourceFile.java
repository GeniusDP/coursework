package org.example.project.checkers.checkstyle;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SourceFile {
  private String fileName;
  private List<StyleError> errors;
}

