package org.example.project.checkers.unittesting;

import java.io.File;
import java.util.Scanner;
import lombok.SneakyThrows;
import org.example.project.checkers.AbstractChecker;
import org.example.project.dtos.unittesting.UnitTestingReport;


public class UnitTestAbstractChecker extends AbstractChecker {

  public UnitTestAbstractChecker(File taskDir) {
    super(taskDir);
  }

  @SneakyThrows
  @Override
  public UnitTestingReport call() {
    String cmd = "mvn clean test -q";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    while (process.isAlive()) {
      if(sc.hasNextLine()) {
        String line = sc.nextLine();
        sb.append(line);
      }
    }
    String allMavenBuildLogs = sb.toString();
    boolean testsPassed = allMavenBuildLogs.isEmpty();
    return new UnitTestingReport(testsPassed ? "TESTS_PASSED" : "SOME_TESTS_FAILED", -1, -1);
  }

}
