package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.services.CheckerService;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checker-service")
public class CheckerController {

  private final SolutionService solutionService;
  private final CheckerService checkerService;

  @PostMapping(path = "/tasks/{taskId}/check-solution", consumes = "multipart/form-data")
  public Solution checkSolution(@PathVariable("taskId") Long taskId, @RequestParam("file") MultipartFile solutionZip) {
    Solution solution = checkerService.registerSolution(taskId, solutionZip);
    return checkerService.checkSolution(solution);
  }

  @GetMapping("/submissions/{id}")
  public Solution getSubmission(@PathVariable Long id) {
    return solutionService.getSubmissionDetails(id);
  }

}
