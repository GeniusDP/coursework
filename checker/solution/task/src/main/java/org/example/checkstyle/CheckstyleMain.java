package org.example.checkstyle;

import java.io.File;
import java.io.IOException;
import org.jdom2.JDOMException;

public class CheckstyleMain {

  public static void main(String[] args) throws IOException, JDOMException {
    CheckstyleReport report = CheckstyleReportParser.parseReport(new File("src/main/resources/checkstyle.xml"));
    System.out.println(report.getSourceFiles().get(1));
  }

}
