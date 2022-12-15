package com.example.solutionservice.services;

import com.example.solutionservice.dto.response.SubmissionDto;
import com.example.solutionservice.dto.response.TaskSourcesDto;
import com.example.solutionservice.entities.Solution;
import com.example.solutionservice.entities.Task;
import com.example.solutionservice.exceptions.SolutionNotFoundException;
import com.example.solutionservice.exceptions.TaskNotFoundException;
import com.example.solutionservice.repositories.SubmissionRepository;
import com.example.solutionservice.repositories.TaskRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SolutionService {

  private final SubmissionRepository submissionRepository;
  private final TaskRepository taskRepository;

  public SubmissionDto getSubmissionById(Long id) {
    Optional<Solution> solutionById = submissionRepository.findById(id);
    if (solutionById.isEmpty()) {
      throw new SolutionNotFoundException();
    }
    Solution solution = solutionById.get();
    byte[] bytes = solution.getSourceInZip();
    return new SubmissionDto(bytes);
  }

  public TaskSourcesDto getTaskSourcesById(Long id) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isEmpty()) {
      throw new TaskNotFoundException();
    }
    Task task = taskOptional.get();
    return new TaskSourcesDto(task.getSourceInZip(), task.getTestSourceInZip());
  }

}
