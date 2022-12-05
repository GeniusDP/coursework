package com.zaranik.coursework.checkerservice.controllers;

import java.io.IOException;
import java.util.Scanner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloDockerController {

  @GetMapping("/run-hello-world-docker")
  public String runDocker0() throws IOException, InterruptedException {
    //String cmd = "docker run hello-world";
    String cmd = "docker run --privileged --network=host -v /var/run/docker.sock:/var/run/docker.sock -e solution_id=2 zaranik/checker:1.0.1";
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);

    StringBuilder sb = new StringBuilder();
    Scanner sc = new Scanner(process.getInputStream());
    int statusCode = process.waitFor();
    System.out.println(statusCode);
    while(sc.hasNextLine()){
      sb.append(sc.nextLine());
    }

    String dockerContainerPrune = "docker container prune --force";
    Process prune = runtime.exec(dockerContainerPrune);
    prune.waitFor();
    return sb.toString();
  }

}
