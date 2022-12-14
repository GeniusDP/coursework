package org.example;

import java.io.File;
import org.example.project.ProjectLoader;
import org.example.project.ProjectUtil;
import org.example.project.checkers.Checker;
import org.example.project.checkers.UnitTestChecker;

public class Main {

  public static void main(String[] args) throws Exception {
    long solutionId = Long.parseLong(System.getenv("SOLUTION_ID"));
    System.out.println("solutionId = " + solutionId);
    File sourceFolder = ProjectLoader.fetchProject(solutionId);

    ProjectUtil projectUtil = new ProjectUtil(sourceFolder);
    boolean projectCompiled = projectUtil.compileProject();

    System.out.println(projectCompiled ? "COMPILED" : "NOT_COMPILED");

    if(projectCompiled) {
      Checker unitTestChecker = new UnitTestChecker(sourceFolder);
      projectUtil.runCheckers(unitTestChecker);
    }
    System.out.println("CHECKING_FINISHED");
  }

}