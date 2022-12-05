package org.example;

import java.io.File;
import java.io.IOException;
import org.example.project.ProjectDao;
import org.example.project.ProjectLoader;
import org.example.project.ProjectUtil;
import org.example.project.checkers.Checker;
import org.example.project.checkers.UnitTestChecker;

public class Main {

  private static final String PATH = "./solution";
  private static final String DB_HOST = "jdbc:postgresql://localhost:5432/mydb";
  private static final String DB_USER = "postgres";
  private static final String DB_PASSWORD = "zaranik";

  public static void main(String[] args) throws IOException {

    Long solutionId = Long.parseLong(System.getenv("solution_id"));

    ProjectDao dao = new ProjectDao(DB_HOST, DB_USER, DB_PASSWORD);

    ProjectLoader projectLoader = new ProjectLoader(PATH, dao);
    File sourceFolder = projectLoader.loadProject(solutionId);

    ProjectUtil projectUtil = new ProjectUtil(sourceFolder);
    boolean projectCompiled = projectUtil.compileProject();

    String compilationStatus = projectCompiled ? "OK" : "ERROR";
    dao.writeCompilationStatus(solutionId, compilationStatus);
    System.out.println(projectCompiled ? "Compiled" : "Not compiled");

    if(projectCompiled) {
      System.out.println("Started testing!");
      Checker unitTestChecker = new UnitTestChecker(solutionId, sourceFolder, dao);
      projectUtil.runCheckers(unitTestChecker);
      System.out.println("Finished testing!");
    }
    System.out.println("Main finished");
  }

}