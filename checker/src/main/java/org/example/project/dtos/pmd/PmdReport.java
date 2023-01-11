package org.example.project.dtos.pmd;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.project.checkers.pmd.SourceFile;

@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class PmdReport {
  List<SourceFile> sourceFiles;
}
