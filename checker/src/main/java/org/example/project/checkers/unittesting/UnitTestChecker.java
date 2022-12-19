package org.example.project.checkers.unittesting;

import java.io.File;
import java.util.Scanner;
import lombok.SneakyThrows;
import org.example.project.checkers.AbstractChecker;
import org.example.project.dtos.unittesting.UnitTestingReport;


public class UnitTestChecker extends AbstractChecker {

  public UnitTestChecker(File taskDir) {
    super(taskDir);
  }

  @SneakyThrows
  @Override
  public UnitTestingReport call() {
    String cmd = "mvn test -q";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    Scanner scanner = new Scanner(process.getInputStream());
    while(process.isAlive()){}
    File reportsFolder = new File(taskDir.getAbsolutePath() + "/target/surefire-reports");
    UnitTestingReport parse = UnitTestReportParser.parse(reportsFolder);
    return parse;
  }

}
