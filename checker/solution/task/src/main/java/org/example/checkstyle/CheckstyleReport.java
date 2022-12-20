package org.example.checkstyle;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckstyleReport {
  private List<SourceFile> sourceFiles;
}
