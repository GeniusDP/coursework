package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.SolutionCheckingFailedException;
import com.zaranik.coursework.checkerservice.repositories.CustomSolutionRepository;
import java.io.IOException;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolutionService {
  
  private final CustomSolutionRepository solutionRepository;
  
  private static final String COMPILED = "COMPILED";
  private static final String NOT_COMPILED = "NOT_COMPILED";
  private static final String TESTS_PASSED = "TESTS_PASSED";
  private static final String SOME_TESTS_FAILED = "SOME_TESTS_FAILED";
  
  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  public CheckingReport performChecking(MultipartFile solutionZip) {
    try {
      Solution solution = new Solution(solutionZip.getBytes());
      Long solutionId = solutionRepository.save(solution).getId();

      int statusCode = runContainer(solutionId);

      System.out.println(statusCode);

      if (statusCode != 0) {
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
    } catch (IOException e) {
      throw new SolutionCheckingFailedException(e);
    }
  }

  private int runContainer(Long solutionId) throws IOException {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId);
    System.out.println(cmd);

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    Scanner scanner = new Scanner(process.getInputStream());
    StringBuilder sb = new StringBuilder();
    while (process.isAlive()) {
      if (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        System.out.println(line);
        sb.append(line);
      }
    }
    scanner.close();
    processSolutionCheckingResult(solutionId, sb);
    return process.exitValue();
  }

  private Solution processSolutionCheckingResult(Long solutionId, StringBuilder sb) {
    Solution solution = solutionRepository.findById(solutionId);
    if(sb.indexOf(COMPILED) == -1) {
      solution.setCompilationStatus(NOT_COMPILED);  
      return solutionRepository.save(solution);
    }
    solution.setCompilationStatus(COMPILED);

    if(sb.indexOf(TESTS_PASSED) == -1) {
      solution.setTestingStatus(SOME_TESTS_FAILED);
      return solutionRepository.save(solution);
    }
    solution.setTestingStatus(TESTS_PASSED);

    /* 
      here should be other logic about testing: prettier 
    */

    return solutionRepository.save(solution);
  }

}
