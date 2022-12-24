package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.TaskRepository;
import com.zaranik.coursework.checkerservice.services.SolutionService.SolutionCheckingResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CheckerService {

  private final SolutionService solutionService;
  private final TaskRepository taskRepository;

  @Transactional
  public Solution registerSolution(Long taskId, MultipartFile solutionZip, String username) {
    Optional<Task> taskOptional = taskRepository.findById(taskId);
    Task task = taskOptional.orElseThrow(TaskNotFoundException::new);
    return solutionService.registerSubmission(task, solutionZip, username);
  }

  @Transactional
  public Solution checkSolution(Solution solution) {
    Task task = solution.getTask();
    SolutionCheckingResult result = solutionService.runContainer(
      solution.getId(),
      task.getId(),
      task.isPmdNeeded(),
      task.isCheckstyleNeeded()
    );
    System.out.println("status = " + result.statusCode);
    if (result.statusCode != 0) {
      throw new ContainerRuntimeException(result.statusCode);
    }
    return solutionService.saveReport(solution, result.fullReport);
  }

}
