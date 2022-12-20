package org.example.pmd;

import java.io.File;
import java.io.IOException;
import org.jdom2.JDOMException;

public class PmdMain {

  public static void main(String[] args) throws IOException, JDOMException {
    PmdReport pmdReport = PmdCheckReportParser.parseReport(new File("src/main/resources/pmd.xml"));
    System.out.println(pmdReport);
  }

}
