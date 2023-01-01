package com.zaranik.coursework.taskmanagementservice.controllers;

import com.zaranik.coursework.taskmanagementservice.aspect.security.basic.SecuredRoute;
import com.zaranik.coursework.taskmanagementservice.aspect.security.roles.teacher.TeacherGrant;
import com.zaranik.coursework.taskmanagementservice.dto.request.TaskCreationDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.TaskResponseDto;
import com.zaranik.coursework.taskmanagementservice.services.TaskService;
import com.zaranik.coursework.taskmanagementservice.utils.JwtTokenUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-management")
public class TaskController {

  private final TaskService taskService;
  private final JwtTokenUtil jwtTokenUtil;

  @GetMapping("/tasks")
  public List<TaskResponseDto> getAllTasks() {
    return taskService.getAllTasks();
  }

  @GetMapping("/tasks/{id}")
  public TaskResponseDto getAllTasks(@PathVariable Long id) {
    return taskService.getTaskById(id);
  }

//  @SecuredRoute
//  @TeacherGrant
  @PostMapping(value = "/tasks", consumes = "multipart/form-data")
  public TaskResponseDto createNewTask(TaskCreationDto dto, @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return taskService.createNewTask(dto, username);
  }

}
