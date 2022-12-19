package org.example.project.checkers.checkstyle;

import java.io.File;
import java.util.Collections;
import lombok.SneakyThrows;
import org.example.project.checkers.AbstractChecker;
import org.example.project.dtos.checkstyle.CheckstyleReport;

public class CheckstyleChecker extends AbstractChecker {

  public CheckstyleChecker(File taskDir) {
    super(taskDir);
  }

  @SneakyThrows
  @Override
  public CheckstyleReport call() {
    String cmd = "mvn checkstyle:check -q";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    while (process.isAlive()) {
    }
    File checkstyleReport = new File(taskDir.getAbsolutePath() + "/target/.xml");
    if (!checkstyleReport.exists()) {
      return new CheckstyleReport(Collections.emptyList());
    }
    return CheckstyleReportParser.parseReport(checkstyleReport);
  }
}
