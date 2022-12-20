package com.zaranik.coursework.checkerservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.SolutionCheckingFailedException;
import com.zaranik.coursework.checkerservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import com.zaranik.coursework.checkerservice.repositories.TaskRepository;
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
  private final TaskRepository taskRepository;
  private final ObjectMapper objectMapper;

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @Transactional(noRollbackFor = {ContainerRuntimeException.class, SolutionCheckingFailedException.class})
  public FullReport performChecking(Long taskId, MultipartFile solutionZip, Boolean pmd, Boolean checkstyle) {
    Optional<Task> taskOptional = taskRepository.findById(taskId);

    Task task = taskOptional.orElseThrow(TaskNotFoundException::new);

    try {
      Solution solution = new Solution(solutionZip.getBytes());

      solution.setTask(task);
      solutionJpaRepository.save(solution);
      Long solutionId = solution.getId();

      SolutionCheckingResult result = runContainer(solutionId, taskId, pmd, checkstyle);
      System.out.println("status = " + result.statusCode);

      if (result.statusCode != 0) {
        throw new ContainerRuntimeException(result.statusCode);
      }
      return result.fullReport;
    } catch (IOException e) {
      throw new SolutionCheckingFailedException(e);
    }
  }

  private SolutionCheckingResult runContainer(Long solutionId, Long taskId, Boolean pmd, Boolean checkstyle) throws IOException {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId, taskId, pmd, checkstyle);
    System.out.println(cmd);

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    Scanner scanner = new Scanner(process.getInputStream());
    StringBuilder sb = new StringBuilder();
    while (process.isAlive()) {
      if (scanner.hasNextLine()) {
        String line = scanner.nextLine();
//        System.out.println(line);
        sb.append(line);
      }
    }
    scanner.close();
    FullReport fullReport = objectMapper.readValue(sb.toString(), FullReport.class);
    return new SolutionCheckingResult(fullReport, process.exitValue());
  }

  @AllArgsConstructor
  static class SolutionCheckingResult {
    public FullReport fullReport;
    public int statusCode;
  }
}


