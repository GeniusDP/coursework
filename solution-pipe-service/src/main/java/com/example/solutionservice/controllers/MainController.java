package com.example.solutionservice.controllers;

import com.example.solutionservice.dto.response.SubmissionDto;
import com.example.solutionservice.dto.response.TaskSourcesDto;
import com.example.solutionservice.services.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/solution-pipe-service")
public class MainController {

  private final SolutionService service;

  @GetMapping("/submissions/sources/{id}")
  public ResponseEntity<SubmissionDto> getSubmissionById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getSubmissionById(id));
  }

  @GetMapping("/tasks/sources/{id}")
  public ResponseEntity<TaskSourcesDto> getTaskSources(@PathVariable Long id) {
    return ResponseEntity.ok(service.getTaskSourcesById(id));
  }

}
