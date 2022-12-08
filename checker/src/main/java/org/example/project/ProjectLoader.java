package org.example.project;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ProjectLoader {

  public static File loadProject() throws Exception {
    Path path = Path.of("solution");
    File sourceFolder = Files.createDirectory(path).toFile();
    System.out.println("*****************");
    String cmd = "unzip ./task.zip -d ./solution";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);

    Scanner scanner = new Scanner(process.getInputStream());
    while (process.isAlive()) {}
    while(scanner.hasNextLine()){
      System.out.println(scanner.nextLine());
    }
    System.out.println(process.exitValue());
    System.out.println("*****************");
    return sourceFolder;
  }

}
