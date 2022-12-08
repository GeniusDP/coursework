package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

  public static void main(String[] args) throws Exception {
    String dbUrl = System.getenv("DB_URL");
    System.out.println("DB_URL = " + dbUrl);
    Connection connection = DriverManager.getConnection(dbUrl);

    PreparedStatement statement = connection.prepareStatement("select id from code_sources");
    ResultSet resultSet = statement.executeQuery();
    while (resultSet.next()){
      System.out.println(resultSet.getLong("id"));
    }
    resultSet.close();
    statement.close();
    connection.close();


/*    File sourceFolder = ProjectLoader.loadProject();
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
    System.out.println("Main finished");*/
  }

}