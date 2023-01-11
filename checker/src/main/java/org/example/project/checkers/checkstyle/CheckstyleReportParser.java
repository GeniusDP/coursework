package org.example.project.checkers.checkstyle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.example.project.dtos.checkstyle.CheckstyleReport;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class CheckstyleReportParser {

  public static CheckstyleReport parseReport(File pmdFile) throws IOException, JDOMException {
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(pmdFile);
    Element element = doc.getRootElement();

    List<SourceFile> sourceFiles = new ArrayList<>();
    for (Element file : element.getChildren()) {
      String fileName = file.getAttributeValue("name");
      List<StyleError> errors = new ArrayList<>();
      for (Element child : file.getChildren()) {
        StyleError error = StyleError.builder()
          .line(child.getAttributeValue("line"))
          .message(child.getAttributeValue("message"))
          .source(child.getAttributeValue("source"))
          .build();
        errors.add(error);
      }
      sourceFiles.add(new SourceFile(fileName, errors));
    }
    return new CheckstyleReport(sourceFiles);
  }
}
