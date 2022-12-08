package org.example;

import java.io.File;
import org.example.project.ProjectLoader;
import org.example.project.ProjectUtil;
import org.example.project.checkers.Checker;
import org.example.project.checkers.UnitTestChecker;

public class Main {

  public static void main(String[] args) throws Exception {
    File sourceFolder = ProjectLoader.loadProject();

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