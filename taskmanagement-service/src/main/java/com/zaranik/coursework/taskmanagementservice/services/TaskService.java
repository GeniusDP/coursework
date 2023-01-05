package com.zaranik.coursework.taskmanagementservice.services;

import com.zaranik.coursework.taskmanagementservice.dto.request.TaskCreationDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.TaskResponseDto;
import com.zaranik.coursework.taskmanagementservice.entities.Task;
import com.zaranik.coursework.taskmanagementservice.exceptions.ForbiddenAccessException;
import com.zaranik.coursework.taskmanagementservice.exceptions.TaskCreationFailedException;
import com.zaranik.coursework.taskmanagementservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.taskmanagementservice.repositories.TaskRepository;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  public List<TaskResponseDto> getAllTasks() {
    return taskRepository.findAll().stream()
      .map(TaskResponseDto::getFromEntity)
      .toList();
  }

  public TaskResponseDto createNewTask(TaskCreationDto dto, String username) {
    int sumPoints = dto.getPmdPoints() + dto.getCheckstylePoints() + dto.getTestPoints();
    if(dto.getCheckstylePoints() > 0 != dto.getCheckstyleNeeded()
      || dto.getPmdPoints() > 0 != dto.getPmdNeeded() || sumPoints != 100){
      throw new TaskCreationFailedException();
    }
    if (dto.getSubmissionsNumberLimit() == null) {
      dto.setSubmissionsNumberLimit(-1);
    }
    try {
      Task newTask = Task.builder()
        .name(dto.getName())
        .description(dto.getDescription())
        .creatorName(username)
        .sourceInZip(dto.getSourceInZip().getBytes())
        .testSourceInZip(dto.getTestSourceInZip().getBytes())
        .checkstyleNeeded(dto.getCheckstyleNeeded())
        .pmdNeeded(dto.getPmdNeeded())
        .testPoints(dto.getTestPoints())
        .pmdPoints(dto.getPmdPoints())
        .checkstylePoints(dto.getCheckstylePoints())
        .submissionsNumberLimit(dto.getSubmissionsNumberLimit())
        .build();
      taskRepository.save(newTask);
      return TaskResponseDto.getFromEntity(newTask);
    } catch (IOException e) {
      throw new TaskCreationFailedException(e);
    }
  }


  public TaskResponseDto getTaskById(Long id) {
    Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    return TaskResponseDto.getFromEntity(task);
  }

  @SneakyThrows
  public TaskResponseDto changeTask(Long taskId, TaskCreationDto dto, String username) {
    Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    if( !task.getCreatorName().equals(username) ){
      throw new ForbiddenAccessException();
    }
    task.setName(dto.getName());
    task.setDescription(dto.getDescription());
    task.setSourceInZip(dto.getSourceInZip().getBytes());
    task.setTestSourceInZip(dto.getTestSourceInZip().getBytes());
    task.setPmdNeeded(dto.getPmdNeeded());
    task.setPmdPoints(dto.getPmdPoints());
    task.setCheckstyleNeeded(dto.getCheckstyleNeeded());
    task.setCheckstylePoints(dto.getCheckstylePoints());
    taskRepository.save(task);
    return TaskResponseDto.getFromEntity(task);
  }

  public byte[] getTaskSources(Long id) {
    Task task = taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    return task.getSourceInZip();
  }

  public void deleteOwnTask(Long taskId, String username) {
    Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    if (task.getCreatorName().equals(username)) {
      taskRepository.delete(task);
    }
  }
}
