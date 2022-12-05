package org.example.project.checkers;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import lombok.SneakyThrows;
import org.example.project.ProjectDao;
import org.example.project.exceptions.NotValidArchiveStructureException;


public class UnitTestChecker extends Checker {

  private static final String BUILD_SUCCESS = "BUILD SUCCESS";

  public UnitTestChecker(Long solutionId, File mainDir, ProjectDao dao) {
    super(solutionId, mainDir, dao);
  }

  @SneakyThrows
  @Override
  public void run() {
    List<File> list = Arrays.stream(mainDir.listFiles())
      .filter(File::isDirectory)
      .toList();
    if (list.size() != 1) {
      throw new NotValidArchiveStructureException();
    }
    File taskDir = list.get(0);

    String cmd = "mvn clean test";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    while (process.isAlive()) {
      if(sc.hasNextLine()) {
        String line = sc.nextLine();
        sb.append(line);
        //System.out.println(line);
      }
    }
    String allMavenBuildLogs = sb.toString();
    boolean testsPassed = allMavenBuildLogs.contains(BUILD_SUCCESS);
    System.out.println(testsPassed ? "TESTS PASSED" : "SOME TESTS FAILED");
    dao.writeTestingStatistics(solutionId, testsPassed ? "TESTS PASSED" : "SOME TESTS FAILED");
  }

}
