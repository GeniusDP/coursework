package org.example.unittests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class UnitTestReportParser {

  public static UnitTestingReport parse(File reportsFolder) {
    List<TestDto> tests = Arrays.stream(reportsFolder.listFiles())
      .filter(file -> file.getName().endsWith(".txt"))
      .map(file -> {
        try {
          String line = Files.lines(file.toPath()).skip(3).limit(1).findAny().get();
          String[] payload = line.split(", ");
          int testsRun = Integer.parseInt(payload[0].split(": ")[1]);
          int testsFailed = Integer.parseInt(payload[1].split(": ")[1] + payload[2].split(": ")[1]);
          return new TestDto(testsRun, testsFailed);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      })
      .toList();

    int testsRun = 0;
    int testsFailed = 0;
    for (TestDto test : tests) {
      testsRun += test.getTestsRun();
      testsFailed += test.getTestsFailed();
    }

    return new UnitTestingReport(testsFailed == 0 ? "TESTS_PASSED" : "SOME_TESTS_FAILED", testsRun, testsFailed);
  }

}
