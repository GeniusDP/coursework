package com.example.solutionservice.controllers;

import com.example.solutionservice.dto.response.SolutionDto;
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

  @GetMapping("/solutions/{id}")
  public ResponseEntity<SolutionDto> getSolutionById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getSolutionById(id));
  }


}
