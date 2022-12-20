package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.TaskRepository;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import com.zaranik.coursework.checkerservice.services.SolutionService.SolutionCheckingResult;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checker-service")
public class CheckerController {

  private final SolutionService solutionService;
  private final TaskRepository taskRepository;

  //@SecuredRoute
  @PostMapping(path = "/tasks/{taskId}/check-solution", consumes = "multipart/form-data")
  public FullReport checkSolution(@PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile solutionZip) {
    Optional<Task> taskOptional = taskRepository.findById(taskId);
    Task task = taskOptional.orElseThrow(TaskNotFoundException::new);
    Solution solution = solutionService.registerSubmission(task, solutionZip);
    try {
      SolutionCheckingResult result = solutionService.runContainer(
        solution.getId(),
        taskId,
        task.isPmdNeeded(),
        task.isCheckstyleNeeded()
      );
      System.out.println("status = " + result.statusCode);
      if (result.statusCode != 0) {
        throw new ContainerRuntimeException(result.statusCode);
      }
      return result.fullReport;
    } catch (IOException e) {
      throw new ContainerRuntimeException(-1);
    }
  }

}
