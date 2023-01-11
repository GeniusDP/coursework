package org.example.project.dtos.checkstyle;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.project.checkers.checkstyle.SourceFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckstyleReport {
  private List<SourceFile> sourceFiles;
}
