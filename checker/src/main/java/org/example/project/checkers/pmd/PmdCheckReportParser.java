package org.example.project.checkers.pmd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.project.dtos.pmd.PmdReport;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class PmdCheckReportParser {

  public static PmdReport parseReport(File pmdFile) throws IOException, JDOMException {
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(pmdFile);
    Element element = doc.getRootElement();

    List<SourceFile> sourceFiles = new ArrayList<>();
    for (Element file : element.getChildren()) {
      String fileName = file.getAttributeValue("name");
      List<Violation> violations = new ArrayList<>();
      for (Element child : file.getChildren()) {
        Violation violation = Violation.builder()
          .value(child.getValue().trim())
          .beginLine(child.getAttributeValue("beginline"))
          .endLine(child.getAttributeValue("endline"))
          .ruleName(child.getAttributeValue("rule"))
          .className(child.getAttributeValue("class"))
          .packageName(child.getAttributeValue("package"))
          .methodName(child.getAttributeValue("method"))
          .build();
        violations.add(violation);
      }
      sourceFiles.add(new SourceFile(fileName, violations));
    }
    return new PmdReport(sourceFiles);
  }
}