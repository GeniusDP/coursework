package com.zaranik.coursework.checkerservice.services;

import static java.time.temporal.ChronoUnit.MINUTES;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleReportEntity;
import com.zaranik.coursework.checkerservice.entities.pmd.PmdReportEntity;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.ContainerTimeLimitExceededException;
import com.zaranik.coursework.checkerservice.exceptions.SolutionCheckingFailedException;
import com.zaranik.coursework.checkerservice.exceptions.SubmissionNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolutionService {

  private final static long MAX_EXECUTION_TIME_MINUTES = 10;

  private final SolutionRepository solutionJpaRepository;
  private final PmdReportService pmdReportService;
  private final CheckstyleReportService checkstyleReportService;
  private final ObjectMapper objectMapper;

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @Transactional
  @SneakyThrows
  public Solution registerSubmission(Task task, MultipartFile solutionZip) {
    try {
      Solution solution = new Solution(solutionZip.getBytes());
      solution.setTask(task);
      solutionJpaRepository.save(solution);
      return solution;
    } catch (IOException e) {
      throw new SolutionCheckingFailedException(e);
    }
  }

  @SneakyThrows
  public SolutionCheckingResult runContainer(Long solutionId, Long taskId, Boolean pmd, Boolean checkstyle) {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId, taskId, pmd, checkstyle);
    System.out.println(cmd);

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    BufferedInputStream inputStream = new BufferedInputStream(process.getInputStream());

    boolean finishedWithoutForcing = process.waitFor(MAX_EXECUTION_TIME_MINUTES, TimeUnit.of(MINUTES));
    if (!finishedWithoutForcing) {
      process.destroy();
      throw new ContainerTimeLimitExceededException();
    }

    String logs = new String(inputStream.readAllBytes());
    FullReport fullReport = objectMapper.readValue(logs, FullReport.class);
    return new SolutionCheckingResult(fullReport, process.exitValue());
  }

  @Transactional
  public Solution saveReport(Solution solution, FullReport report) {
    solution.setCompilationStatus(report.getCompilationReport().getCompilationStatus().name());

    int testRun = report.getUnitTestingReport().getTestRun();
    int testFailed = report.getUnitTestingReport().getTestFailed();
    String testingStatus = report.getUnitTestingReport().getMessage();
    solution.setTestsRun(testRun);
    solution.setTestsPassed(testRun - testFailed);
    solution.setTestingStatus(testingStatus);

    PmdReportEntity pre = pmdReportService.savePmdReport(report.getPmdReport());
    solution.setPmdReportEntity(pre);

    CheckstyleReportEntity cre = checkstyleReportService.saveCheckstyleReport(report.getCheckstyleReport());
    solution.setCheckstyleReportEntity(cre);

    return solutionJpaRepository.save(solution);
  }

  public Solution getSubmissionDetails(Long id) {
    return solutionJpaRepository.findById(id).orElseThrow(SubmissionNotFoundException::new);
  }

  @AllArgsConstructor
  public static class SolutionCheckingResult {
    public FullReport fullReport;
    public int statusCode;
  }
}


