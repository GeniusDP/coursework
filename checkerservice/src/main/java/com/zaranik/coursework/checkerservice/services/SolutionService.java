package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.SolutionCheckingFailedException;
import com.zaranik.coursework.checkerservice.repositories.CustomSolutionRepository;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolutionService {
  
  private final SolutionRepository solutionJpaRepository;

  private static final String COMPILED = "COMPILED";
  private static final String NOT_COMPILED = "NOT_COMPILED";
  private static final String TESTS_PASSED = "TESTS_PASSED";
  private static final String SOME_TESTS_FAILED = "SOME_TESTS_FAILED";
  
  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @Transactional(noRollbackFor = {ContainerRuntimeException.class, SolutionCheckingFailedException.class})
  public CheckingReport performChecking(MultipartFile solutionZip) {
    try {
      Solution solution = new Solution(solutionZip.getBytes());
      solutionJpaRepository.save(solution);
      Long solutionId = solution.getId();

      SolutionCheckingResult result = runContainer(solutionId);
      System.out.println(result.statusCode);

      if (true) {
        throw new ContainerRuntimeException();
      }
      solution.setTestingStatus(result.solution.testingStatus);
      solution.setCompilationStatus(result.solution.compilationStatus);
      solutionJpaRepository.save(solution);
      System.out.println(solution);
      return new CheckingReport(
        solution.getId(),
        solution.getCompilationStatus(),
        solution.getTestsNumber(),
        solution.getTestsPassed(),
        solution.getTestingStatus()
      );
    } catch (IOException e) {
      throw new SolutionCheckingFailedException(e);
    }
  }

  private SolutionCheckingResult runContainer(Long solutionId) throws IOException {
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
    SolutionDto solution = processSolutionCheckingResult(solutionId, sb);
    return new SolutionCheckingResult(solution, process.exitValue());
  }

  private SolutionDto processSolutionCheckingResult(Long solutionId, StringBuilder sb) {

    System.out.println("processSolutionCheckingResult");
    if(sb.indexOf(COMPILED) == -1) {
      return new SolutionDto(NOT_COMPILED, SOME_TESTS_FAILED);
    }

    if(sb.indexOf(TESTS_PASSED) == -1) {
      return new SolutionDto(COMPILED, SOME_TESTS_FAILED);
    }
    /*
      here should be other logic about testing: prettier, linter
    */
    SolutionDto solutionDto = new SolutionDto(COMPILED, TESTS_PASSED);
    System.out.println("finally: " + solutionDto);
    return solutionDto;
  }

}

@AllArgsConstructor
class SolutionDto {
  public String compilationStatus;
  public String testingStatus;
}

@AllArgsConstructor
class SolutionCheckingResult {
  public SolutionDto solution;
  public int statusCode;
}
