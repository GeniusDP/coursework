package com.zaranik.coursework.checkerservice.dtos.container.response.pmd;

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
  private List<Violation> violations;
}
