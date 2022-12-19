package org.example.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import org.example.project.checkers.Checker;
import org.example.project.dtos.checkstyle.CheckstyleReport;
import org.example.project.dtos.pmd.PmdReport;
import org.example.project.dtos.unittesting.UnitTestingReport;
import org.example.project.exceptions.CheckerException;

@AllArgsConstructor
public class ProjectUtil {

  private static final int MAX_THREAD_POOL_SIZE = 5;

  private File taskDir;

  public boolean compileProject() throws IOException {
    String cmd = "mvn clean compile -q";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    while (process.isAlive()) {
      if (sc.hasNextLine()) {
        String line = sc.nextLine();
        sb.append(line);
        System.out.println(line);
      }
    }
    sc.close();
    String allMavenBuildLogs = sb.toString();
    return allMavenBuildLogs.isEmpty();
  }

  public FullReport runCheckers(List<Checker> checkerList, FullReport fullReport) {
    int threadPoolSize = Math.min(checkerList.size(), MAX_THREAD_POOL_SIZE);
    ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
    List<Future<?>> futureCheckersList = new ArrayList<>();
    for (var checker : checkerList) {
      Future<?> submit = executorService.submit(checker);
      futureCheckersList.add(submit);
    }

    try {
      for (var future : futureCheckersList) {
        Object report = future.get();
        if(report instanceof UnitTestingReport unitTestingReport) {
          fullReport.setUnitTestingReport(unitTestingReport);
        } else if(report instanceof PmdReport pmdReport) {
          fullReport.setPmdReport(pmdReport);
        } else if(report instanceof CheckstyleReport checkstyleReport) {
          fullReport.setCheckstyleReport(checkstyleReport);
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new CheckerException(e);
    } finally {
      executorService.shutdown();
    }
    return fullReport;
  }

}
