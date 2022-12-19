package org.example;

import static org.example.project.dtos.compilation.CompilationStatus.COMPILED;
import static org.example.project.dtos.compilation.CompilationStatus.NOT_COMPILED;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.example.project.FullReport;
import org.example.project.ProjectLoader;
import org.example.project.ProjectUtil;
import org.example.project.checkers.Checker;
import org.example.project.checkers.checkstyle.CheckstyleChecker;
import org.example.project.dtos.compilation.CompilationReport;
import org.example.project.checkers.pmd.PmdChecker;
import org.example.project.checkers.unittesting.UnitTestAbstractChecker;

public class Main {

  public static void main(String[] args) throws Exception {
    long solutionId = Long.parseLong(System.getProperty("SOLUTION_ID", "1"));
    long taskId = Long.parseLong(System.getProperty("TASK_ID", "1"));
    boolean checkPmd = Boolean.parseBoolean(System.getProperty("PMD", "true"));
    boolean useCheckstyle = Boolean.parseBoolean(System.getProperty("CHECKSTYLE", "true"));

    System.out.println("solutionId = " + solutionId);
    System.out.println("taskId = " + taskId);
    System.out.println("checkPmd = " + checkPmd);
    System.out.println("useCheckstyle = " + useCheckstyle);

    File taskDir = ProjectLoader.fetchProject(taskId, solutionId);

    ProjectUtil projectUtil = new ProjectUtil(taskDir);
    boolean projectCompiled = projectUtil.compileProject();

    FullReport fullReport = new FullReport();
    CompilationReport compilationReport = new CompilationReport(projectCompiled ? COMPILED : NOT_COMPILED);
    fullReport.setCompilationReport(compilationReport);
    if(projectCompiled) {
      Checker unitTestChecker = new UnitTestAbstractChecker(taskDir);
      List<Checker> checkerList = new ArrayList<>();
      checkerList.add(unitTestChecker);

      if(checkPmd){
        checkerList.add(new PmdChecker(taskDir));
      }

      if(useCheckstyle){
        checkerList.add(new CheckstyleChecker(taskDir));
      }

      projectUtil.runCheckers(checkerList, fullReport);
    }
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonFullReport = objectMapper.writeValueAsString(fullReport);
    System.out.println(jsonFullReport);
  }

}