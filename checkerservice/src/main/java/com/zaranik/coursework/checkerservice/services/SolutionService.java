package com.zaranik.coursework.checkerservice.services;

import static java.time.temporal.ChronoUnit.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleReportEntity;
import com.zaranik.coursework.checkerservice.entities.pmd.PmdReportEntity;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.SolutionCheckingFailedException;
import com.zaranik.coursework.checkerservice.exceptions.SubmissionNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.PmdReportRepository;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SolutionService {

  private final static long MAX_EXECUTION_TIME = MILLISECONDS.convert(Duration.of(10, MINUTES));

  private final SolutionRepository solutionJpaRepository;
  private final PmdReportService pmdReportService;
  private final CheckstyleReportService checkstyleReportService;
  private final ObjectMapper objectMapper;

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @Transactional(noRollbackFor = {ContainerRuntimeException.class, SolutionCheckingFailedException.class})
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

  public SolutionCheckingResult runContainer(Long solutionId, Long taskId, Boolean pmd, Boolean checkstyle) throws IOException {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId, taskId, pmd, checkstyle);
    System.out.println(cmd);

    long startTime = System.currentTimeMillis();
    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    Scanner scanner = new Scanner(process.getInputStream());
    StringBuilder sb = new StringBuilder();
    while (process.isAlive() && (System.currentTimeMillis() - startTime) < MAX_EXECUTION_TIME) {
      if (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        sb.append(line);
      }
    }
    scanner.close();
    FullReport fullReport = objectMapper.readValue(sb.toString(), FullReport.class);
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


