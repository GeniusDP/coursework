package org.example.pmd;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class PmdReport {
  List<SourceFile> sourceFiles;
}
