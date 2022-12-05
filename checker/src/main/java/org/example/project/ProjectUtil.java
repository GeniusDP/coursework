package org.example.project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.project.checkers.Checker;
import org.example.project.exceptions.CheckerException;
import org.example.project.exceptions.NotValidArchiveStructureException;

@AllArgsConstructor
@Slf4j
public class ProjectUtil {

  private static final String BUILD_SUCCESS = "BUILD SUCCESS";
  private static final int MAX_THREAD_POOL_SIZE = 5;

  private File mainDir;

  public boolean compileProject() throws IOException {
    List<File> list = Arrays.stream(mainDir.listFiles())
      .filter(File::isDirectory)
      .toList();
    if (list.size() != 1) {
      throw new NotValidArchiveStructureException();
    }
    File taskDir = list.get(0);
    String cmd = "mvn clean compile";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd, null, taskDir);
    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    while (process.isAlive()) {
      if(sc.hasNextLine()) {
        String line = sc.nextLine();
        sb.append(line);
        System.out.println(line);
      }
    }
    String allMavenBuildLogs = sb.toString();
    return allMavenBuildLogs.contains(BUILD_SUCCESS);
  }

  public void runCheckers(Checker... checkers) {
    List<Checker> checkerList = List.of(checkers);

    int threadPoolSize = Math.min(checkerList.size(), MAX_THREAD_POOL_SIZE);
    ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
    List<Future<?>> futureCheckersList = new ArrayList<>();
    for (var checker : checkerList) {
      Future<?> submit = executorService.submit(checker);
      futureCheckersList.add(submit);
    }

    try {
      for (var future : futureCheckersList) {
        future.get();
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new CheckerException(e);
    }
    executorService.shutdown();
  }

}
