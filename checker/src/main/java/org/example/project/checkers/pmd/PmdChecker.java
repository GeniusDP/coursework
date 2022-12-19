package org.example.project.checkers.pmd;

import java.io.File;
import java.util.Collections;
import lombok.SneakyThrows;
import org.example.project.checkers.AbstractChecker;
import org.example.project.dtos.pmd.PmdReport;

public class PmdChecker extends AbstractChecker {

  public PmdChecker(File taskDir) {
    super(taskDir);
  }

  @SneakyThrows
  @Override
  public PmdReport call() {
    String cmd = "mvn pmd:pmd -q";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    process.waitFor();
    File pmdReport = new File(taskDir.getAbsolutePath() + "/target/pmd.xml");
    if (!pmdReport.exists()) {
      return new PmdReport(Collections.emptyList());
    }
    return PmdCheckReportParser.parseReport(pmdReport);
  }
}