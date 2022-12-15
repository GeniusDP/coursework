package com.zaranik.coursework.taskmanagementservice.services;

import com.zaranik.coursework.taskmanagementservice.dto.request.TaskCreationDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.SubmissionResponseDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.TaskResponseDto;
import com.zaranik.coursework.taskmanagementservice.entities.Submission;
import com.zaranik.coursework.taskmanagementservice.entities.Task;
import com.zaranik.coursework.taskmanagementservice.exceptions.TaskCreationFailedException;
import com.zaranik.coursework.taskmanagementservice.repositories.SubmissionRepository;
import com.zaranik.coursework.taskmanagementservice.repositories.TaskRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;
  private final SubmissionRepository submissionRepository;

  public List<TaskResponseDto> getAllTasks() {
    return taskRepository.findAll().stream()
      .map(task -> new TaskResponseDto(task.getId(), task.getName()))
      .toList();
  }

  public TaskResponseDto createNewTask(TaskCreationDto dto) {
    try {
      Task newTask = Task.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .sourceInZip(dto.getSourceInZip().getBytes())
        .testSourceInZip(dto.getTestSourceInZip().getBytes())
        .build();
      taskRepository.save(newTask);
      return new TaskResponseDto(newTask.getId(), newTask.getName());
    } catch (IOException e) {
      throw new TaskCreationFailedException(e);
    }
  }

  public List<SubmissionResponseDto> getAllSubmissionsOfTask(Long taskId) {
    List<Submission> allSubmissions = submissionRepository.getAllSubmissionsOfTask(taskId);
    return allSubmissions.stream()
      .map(s -> SubmissionResponseDto.builder()
        .id(s.getId())
        .userId(s.getUserId())
        .compilationStatus(s.getCompilationStatus())
        .testingStatus(s.getTestingStatus())
        .testsNumber(s.getTestsNumber())
        .testsPassed(s.getTestsPassed())
        .build())
      .toList();
  }
}
