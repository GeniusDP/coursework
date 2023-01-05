package com.zaranik.coursework.checkerservice.services;

import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.ContainerTimeLimitExceededException;
import com.zaranik.coursework.checkerservice.exceptions.NoSubmissionsLeftException;
import com.zaranik.coursework.checkerservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.TaskRepository;
import com.zaranik.coursework.checkerservice.services.SolutionService.SolutionCheckingResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckerService {

  private final SolutionService solutionService;
  private final TaskRepository taskRepository;

  @Transactional
  public Solution registerSolution(Long taskId, MultipartFile solutionZip, String username) {
    if(submissionsLeft(taskId, username) == 0){
      throw new NoSubmissionsLeftException();
    }
    return solutionService.registerSubmission(taskId, solutionZip, username);
  }

  public long submissionsLeft(Long taskId, String username) {
    Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    if(task.getSubmissionsNumberLimit() == -1 || task.getCreatorName().equals(username)){
      return 1_000_000_000L;
    }
    int submissionsDone = solutionService.submissionsDoneOnTaskByUser(taskId, username);
    return Math.max(0, task.getSubmissionsNumberLimit() - submissionsDone);
  }

  @Transactional(noRollbackFor = {ContainerRuntimeException.class, ContainerTimeLimitExceededException.class})
  public Solution checkSolution(Solution solution) {
    Task task = solution.getTask();
    SolutionCheckingResult result = solutionService.runContainer(
      solution.getId(),
      task.getId(),
      task.isPmdNeeded(),
      task.isCheckstyleNeeded()
    );
    log.info("status = {}", result.statusCode);
    if (result.statusCode != 0) {
      throw new ContainerRuntimeException(result.statusCode);
    }
    return solutionService.saveReport(solution, result.fullReport);
  }

}
