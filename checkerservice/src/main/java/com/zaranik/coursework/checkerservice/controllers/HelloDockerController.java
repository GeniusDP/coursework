package com.zaranik.coursework.checkerservice.controllers;

import java.io.IOException;
import java.util.Scanner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloDockerController {

  @GetMapping("/run-hello-world-docker")
  public String runDocker0() throws IOException, InterruptedException {
    String cmd = "docker run --privileged -v /var/run/docker.sock:/var/run/docker.sock hello-world";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);

    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    int statusCode = process.waitFor();
    System.out.println(statusCode);
    while (sc.hasNextLine()) {
      sb.append(sc.nextLine());
    }
    return sb.toString();
  }

  @GetMapping("/get-sum")
  public String runDockerSum() throws IOException, InterruptedException {
    String cmd = "echo \"10 12\" | docker run -i --privileged -v /var/run/docker.sock:/var/run/docker.sock aplusb:1.0";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);

    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    int statusCode = process.waitFor();
    System.out.println(statusCode);
    while (sc.hasNextLine()) {
      sb.append(sc.nextLine());
    }
    return sb.toString();
  }

  @GetMapping("/volumes")
  public String runDockerWithVolumes() throws IOException, InterruptedException {
    String cmd = "docker run --privileged -v /var/run/docker.sock:/var/run/docker.sock aplusb:1.0";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);

    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    int statusCode = process.waitFor();
    System.out.println(statusCode);
    while (sc.hasNextLine()) {
      sb.append(sc.nextLine());
    }
    return sb.toString();
  }

}
