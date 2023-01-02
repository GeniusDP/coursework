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
    String cmd = "mvn test";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    Scanner scanner = new Scanner(process.getInputStream());
    StringBuilder result = new StringBuilder();
    while (process.isAlive()) {
      if (scanner.hasNextLine()) {
        result.append(scanner.nextLine());
      }
    }
    return UnitTestReportParser.parseLogString(result.toString());
  }

}
