package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.dtos.CheckingReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import com.zaranik.coursework.checkerservice.services.SolutionService.SolutionCheckingResult;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checker-service")
public class CheckerController {

  private final SolutionService solutionService;

  private final SolutionRepository solutionRepository;

  //@SecuredRoute
  @PostMapping(path = "/tasks/{taskId}/check-solution", consumes = "multipart/form-data")
  public ResponseEntity<FullReport> checkSolution(
    @PathVariable("taskId") Long taskId,
    @RequestParam("file") MultipartFile solutionZip,
    @RequestParam("pmd") Boolean pmd,
    @RequestParam("checkstyle") Boolean checkstyle
  ) {
    Long solutionId = solutionService.performChecking(taskId, solutionZip, pmd, checkstyle);

    try {
      SolutionCheckingResult result = solutionService.runContainer(solutionId, taskId, pmd, checkstyle);
      System.out.println("status = " + result.statusCode);
      if (result.statusCode != 0) {
        throw new ContainerRuntimeException(result.statusCode);
      }
      return ResponseEntity.ok(result.fullReport);
    } catch (IOException e) {
      throw new ContainerRuntimeException(-1);
    }
  }

  @GetMapping("/solutions/stats/{id}")
  public CheckingReport getSolutionCheckingReport(@PathVariable Long id) {
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
