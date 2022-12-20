package com.zaranik.coursework.taskmanagementservice.services;

import com.zaranik.coursework.taskmanagementservice.dto.request.TaskCreationDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.TaskResponseDto;
import com.zaranik.coursework.taskmanagementservice.entities.Task;
import com.zaranik.coursework.taskmanagementservice.exceptions.TaskCreationFailedException;
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

  public List<TaskResponseDto> getAllTasks() {
    return taskRepository.findAll().stream()
      .map(task -> new TaskResponseDto(task.getId(), task.getName()))
      .toList();
  }

  public TaskResponseDto createNewTask(TaskCreationDto dto) {
    int sumPoints = dto.getPmdPoints() + dto.getCheckstylePoints() + dto.getTestPoints();
    if (sumPoints != 100) {
      throw new TaskCreationFailedException();
    }
    try {
      Task newTask = Task.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .sourceInZip(dto.getSourceInZip().getBytes())
        .testSourceInZip(dto.getTestSourceInZip().getBytes())
        .checkstyleNeeded(dto.getCheckstyleNeeded())
        .pmdNeeded(dto.getPmdNeeded())
        .testPoints(dto.getTestPoints())
        .pmdPoints(dto.getPmdPoints())
        .checkstylePoints(dto.getCheckstylePoints())
        .build();
      taskRepository.save(newTask);
      return new TaskResponseDto(newTask.getId(), newTask.getName());
    } catch (IOException e) {
      throw new TaskCreationFailedException(e);
    }
  }


}
