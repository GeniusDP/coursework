package org.example;

import java.io.File;
import org.example.project.ProjectLoader;
import org.example.project.ProjectUtil;
import org.example.project.checkers.Checker;
import org.example.project.checkers.UnitTestChecker;

public class Main {

  public static void main(String[] args) throws Exception {
    String dbUrl = System.getenv("DB_URL");
    System.out.println("dbUrl = " + dbUrl);
    String SOLUTION_ID = System.getenv("SOLUTION_ID");
    System.out.println("SOLUTION_ID = " + SOLUTION_ID);
    long solutionId = Long.parseLong(SOLUTION_ID);
    System.out.println("solutionId = " + solutionId);
    File sourceFolder = ProjectLoader.fetchProject(solutionId);
    System.out.println(sourceFolder.getAbsolutePath());

    ProjectUtil projectUtil = new ProjectUtil(sourceFolder);
    boolean projectCompiled = projectUtil.compileProject();

    System.out.println(projectCompiled ? "Compiled" : "Not compiled");

    if(projectCompiled) {
      System.out.println("Started testing!");
      Checker unitTestChecker = new UnitTestChecker(sourceFolder);
      projectUtil.runCheckers(unitTestChecker);
      System.out.println("Finished testing!");
    }
    System.out.println("Main finished");
  }

}