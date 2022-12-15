package com.zaranik.coursework.taskmanagementservice.controllers;

import com.zaranik.coursework.taskmanagementservice.dto.request.TaskCreationDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.SubmissionResponseDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.TaskResponseDto;
import com.zaranik.coursework.taskmanagementservice.services.TaskService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-management")
public class TaskController {

  private final TaskService taskService;

  @GetMapping("/tasks")
  public List<TaskResponseDto> getAllTasks(){
    return taskService.getAllTasks();
  }

  @PostMapping(value = "/tasks", consumes = "multipart/form-data")
  public TaskResponseDto createNewTask(TaskCreationDto dto) {
    return taskService.createNewTask(dto);
  }

  @GetMapping("/tasks/{taskId}/submissions")
  public List<SubmissionResponseDto> getAllSubmissionsOfTask(@PathVariable Long taskId) {
    return taskService.getAllSubmissionsOfTask(taskId);
  }




}
