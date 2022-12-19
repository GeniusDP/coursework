package org.example.project.dtos.pmd;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.example.project.checkers.pmd.SourceFile;

@Data
@ToString
@AllArgsConstructor
public class PmdReport {
  List<SourceFile> sourceFiles;
}
