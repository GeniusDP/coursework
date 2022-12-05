package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.repositories.CustomSolutionRepository;
import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolutionService {

  private final CustomSolutionRepository solutionRepository;

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @SneakyThrows
  public CheckingReport performChecking(MultipartFile solutionZip) {
    Solution solution = new Solution(solutionZip.getBytes());
    solutionRepository.save(solution);
    Long solutionId = solution.getId();

    String containerName = UUID.randomUUID().toString();

    int statusCode = runContainer(containerName, solutionId);
    System.out.println(statusCode);
    removeContainer(containerName);

    if(statusCode != 0){
      throw new ContainerRuntimeException();
    }
    Solution result = solutionRepository.findById(solutionId);
    return new CheckingReport(
      result.getId(),
      result.getCompilationStatus(),
      result.getTestsNumber(),
      result.getTestsPassed(),
      result.getTestingStatus()
    );
  }

  private int runContainer(String containerName, Long solutionId) throws IOException {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId, containerName);
    System.out.println("cmd = " + cmd);

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    Scanner scanner = new Scanner(process.getInputStream());
    while (process.isAlive()) {
      if(scanner.hasNextLine()){
        System.out.println(scanner.nextLine());
      }
    }
    return process.exitValue();
  }

  private void removeContainer(String containerName) throws IOException {
    String dockerContainerPrune = String.format("docker rm %s --force", containerName);
    Runtime runtime = Runtime.getRuntime();
    runtime.exec(dockerContainerPrune);
  }

}
