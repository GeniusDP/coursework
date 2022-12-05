package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MainController {

  @Autowired
  private SolutionService solutionService;

  @Autowired
  private SolutionRepository solutionRepository;

  @PostMapping(path = "/check-solution", consumes = "multipart/form-data")
  public CheckingReport checkSolution(@RequestParam("file") MultipartFile solutionZip) {
    return solutionService.performChecking(solutionZip);
  }

  @GetMapping("/solution-report/{id}")
  public CheckingReport checkingReport(@PathVariable Long id){
    Solution result = solutionRepository.findById(id).get();
    return new CheckingReport(
      result.getId(),
      result.getCompilationStatus(),
      result.getTestsNumber(),
      result.getTestsPassed(),
      result.getTestingStatus()
    );
  }

}
