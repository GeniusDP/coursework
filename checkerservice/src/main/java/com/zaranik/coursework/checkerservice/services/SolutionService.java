package com.zaranik.coursework.checkerservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.SolutionCheckingFailedException;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
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
  private final ObjectMapper objectMapper;
  private final static long MAX_EXECUTION_TIME = TimeUnit.MILLISECONDS.convert(Duration.of(10, ChronoUnit.MINUTES));

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @Transactional(noRollbackFor = {ContainerRuntimeException.class, SolutionCheckingFailedException.class})
  public Solution registerSubmission(Task task, MultipartFile solutionZip) {
    try {
      Solution solution = new Solution(solutionZip.getBytes());
      solution.setTask(task);
      solutionJpaRepository.save(solution);
      return solution;
    } catch (IOException e) {
      throw new SolutionCheckingFailedException(e);
    }
  }

  public SolutionCheckingResult runContainer(Long solutionId, Long taskId, Boolean pmd, Boolean checkstyle) throws IOException {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId, taskId, pmd, checkstyle);
    System.out.println(cmd);

    long startTime = System.currentTimeMillis();
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    Scanner scanner = new Scanner(process.getInputStream());
    StringBuilder sb = new StringBuilder();
    while (process.isAlive() && (System.currentTimeMillis() - startTime) < MAX_EXECUTION_TIME) {
      if (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        sb.append(line);
      }
    }
    scanner.close();
    FullReport fullReport = objectMapper.readValue(sb.toString(), FullReport.class);
    return new SolutionCheckingResult(fullReport, process.exitValue());
  }

  @AllArgsConstructor
  public static class SolutionCheckingResult {
    public FullReport fullReport;
    public int statusCode;
  }
}


