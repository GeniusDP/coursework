package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MainController {

  private final SolutionService solutionService;

  private final SolutionRepository solutionRepository;

  //@SecuredRoute
  @PostMapping(path = "/check-solution", consumes = "multipart/form-data")
  public CheckingReport checkSolution(@RequestParam("file") MultipartFile solutionZip) {
    return solutionService.performChecking(solutionZip);
  }

  @GetMapping("/solutions/{id}")
  public CheckingReport getSolutionCheckingReport(@PathVariable Long id){
    Solution solution = solutionRepository.findById(id).get();
    return new CheckingReport(
      solution.getId(),
      solution.getCompilationStatus(),
      solution.getTestsNumber(),
      solution.getTestsPassed(),
      solution.getTestingStatus()
    );
  }

}
