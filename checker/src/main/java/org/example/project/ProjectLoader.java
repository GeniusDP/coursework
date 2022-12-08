package org.example.project;

import java.io.File;
import java.util.Scanner;

public class ProjectLoader {

  public static File loadProject() throws Exception {
    File sourceFolder = new File("./solution");
    System.out.println("*****************");
    String cmd = "unzip ./task.zip -d ./solution";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);

    Scanner scanner = new Scanner(process.getInputStream());
    while(process.isAlive()){
      if(scanner.hasNextLine()){
        System.out.println(scanner.nextLine());
      }
    }
    System.out.println("*****************");
    return sourceFolder;
  }

}
