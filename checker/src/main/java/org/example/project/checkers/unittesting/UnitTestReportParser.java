package org.example.project.checkers.unittesting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.example.project.dtos.unittesting.UnitTestingReport;

public class UnitTestReportParser {

  public static int lastIndexOfRegex(String str, String toFind) {
    Pattern pattern = Pattern.compile(toFind);
    Matcher matcher = pattern.matcher(str);
    int lastIndex = -1;
    while (matcher.find()) {
      lastIndex = matcher.start();
    }
    return lastIndex;
  }

  public static UnitTestingReport parseLogString(String logs) {
    try{ 
      int indexOfRegex = lastIndexOfRegex(logs, "Tests run: \\d+, Failures: \\d+, Errors: \\d+, Skipped: \\d+");
      StringBuilder result = new StringBuilder();
      int i = indexOfRegex;
      while(i < logs.length() && logs.charAt(i) != '\n'){
        result.append(logs.charAt(i));
        i++;
      }
      String[] payload = result.toString().split(", ");
      int testsRun = Integer.parseInt(payload[0].split(": ")[1]);
      int testsFailed = Integer.parseInt(payload[1].split(": ")[1]) + Integer.parseInt(payload[2].split(": ")[1]);
      return new UnitTestingReport(testsFailed == 0 ? "TESTS_PASSED" : "SOME_TESTS_FAILED", testsRun, testsFailed);
    } catch(Exception e) {
      System.out.println(logs);
      return null;
    }
  }

}
