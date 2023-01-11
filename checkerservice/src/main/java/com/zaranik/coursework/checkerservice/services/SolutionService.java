package com.zaranik.coursework.checkerservice.services;

import static java.time.temporal.ChronoUnit.MINUTES;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaranik.coursework.checkerservice.dtos.container.response.FullReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.checkstyle.CheckstyleReport;
import com.zaranik.coursework.checkerservice.dtos.container.response.pmd.PmdReport;
import com.zaranik.coursework.checkerservice.entities.RuntimeStatus;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.entities.Task;
import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleReportEntity;
import com.zaranik.coursework.checkerservice.entities.pmd.PmdReportEntity;
import com.zaranik.coursework.checkerservice.exceptions.ContainerRuntimeException;
import com.zaranik.coursework.checkerservice.exceptions.ContainerTimeLimitExceededException;
import com.zaranik.coursework.checkerservice.exceptions.ForbiddenAccessException;
import com.zaranik.coursework.checkerservice.exceptions.SubmissionNotFoundException;
import com.zaranik.coursework.checkerservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.checkerservice.repositories.SolutionRepository;
import com.zaranik.coursework.checkerservice.repositories.TaskRepository;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class SolutionService {

  private final SolutionRepository solutionJpaRepository;
  private final PmdReportService pmdReportService;
  private final CheckstyleReportService checkstyleReportService;
  private final ObjectMapper objectMapper;
  private final TaskRepository taskRepository;

  @Value("${container.docker.start-command}")
  private String dockerStartCommand;

  @Value("${container.docker.max-execution-time-minutes}")
  private Integer maxExecutionTimeMinutes;

  @Transactional
  public Solution registerSubmission(Long taskId, MultipartFile solutionZip, String username) {
    Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    try {
      Solution solution = new Solution(solutionZip.getBytes());
      solution.setUserUsername(username);
      solution.setTask(task);
      solution.setRuntimeStatus(RuntimeStatus.REGISTERED);
      solutionJpaRepository.save(solution);
      return solution;
    } catch (IOException e) {
      throw new ContainerRuntimeException(100);
    }
  }

  @SneakyThrows
  public SolutionCheckingResult runContainer(Long solutionId, Long taskId, Boolean pmd, Boolean checkstyle) {
    String cmdTemplate = dockerStartCommand;
    String cmd = String.format(cmdTemplate, solutionId, taskId, pmd, checkstyle);
    log.info(cmd);

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(cmd);
    BufferedInputStream inputStream = new BufferedInputStream(process.getInputStream());

    boolean finishedWithoutForcing = process.waitFor(maxExecutionTimeMinutes, TimeUnit.of(MINUTES));
    if (!finishedWithoutForcing) {
      process.destroy();
      Solution solution = solutionJpaRepository.getById(solutionId);
      solution.setRuntimeStatus(RuntimeStatus.TIME_LIMIT_EXCEEDED);
      solutionJpaRepository.save(solution);
      throw new ContainerTimeLimitExceededException();
    }

    String logs = new String(inputStream.readAllBytes());
    int exitCode = process.exitValue();
    try {
      FullReport fullReport = objectMapper.readValue(logs, FullReport.class);
      Solution solution = solutionJpaRepository.getById(solutionId);
      solution.setRuntimeStatus(RuntimeStatus.FAILED_IN_RUNTIME);
      solutionJpaRepository.save(solution);
      return new SolutionCheckingResult(fullReport, exitCode);
    } catch (JacksonException e) {
      Solution solution = solutionJpaRepository.getById(solutionId);
      solution.setRuntimeStatus(RuntimeStatus.FAILED_IN_RUNTIME);
      solutionJpaRepository.save(solution);
      throw new ContainerRuntimeException(exitCode);
    }
  }

  @Transactional
  public Solution saveReport(Solution solution, FullReport report) {
    solution.setCompilationStatus(report.getCompilationReport().getCompilationStatus().name());
    int testRun = -1;
    int testFailed = -1;
    int testsPassed = -1;
    if(report.getUnitTestingReport() != null) {
      testRun = report.getUnitTestingReport().getTestRun();
      testFailed = report.getUnitTestingReport().getTestFailed();
      testsPassed = testRun - testFailed;
      String testingStatus = report.getUnitTestingReport().getMessage();
      solution.setTestsRun(testRun);
      solution.setTestsPassed(testsPassed);
      solution.setTestingStatus(testingStatus);
    }

    PmdReport pmdReport = report.getPmdReport();
    if (pmdReport != null) {
      pmdReport.setSourceFiles(pmdReport.getSourceFiles().stream().filter(f -> f.getFileName() != null).toList());
    }
    PmdReportEntity pre = pmdReportService.savePmdReport(pmdReport);
    solution.setPmdReportEntity(pre);

    CheckstyleReport checkstyleReport = report.getCheckstyleReport();
    if (checkstyleReport != null) {
      checkstyleReport.setSourceFiles(checkstyleReport.getSourceFiles().stream().filter(f -> f.getErrors().size() != 0).toList());
    }
    CheckstyleReportEntity cre = checkstyleReportService.saveCheckstyleReport(report.getCheckstyleReport());
    solution.setCheckstyleReportEntity(cre);

    if (testRun > 0) {
      Task task = solution.getTask();
      double totalScore = task.getTestPoints() * (testsPassed + 0.) / testRun;
      if (pmdReport == null || pmdReport.getSourceFiles().isEmpty()) {
        totalScore += task.getPmdPoints();
      }

      if (checkstyleReport == null || checkstyleReport.getSourceFiles().isEmpty()) {
        totalScore += task.getPmdPoints();
      }
      solution.setTotalScore(totalScore);
    }
    solution.setRuntimeStatus(RuntimeStatus.CHECKED);
    return solutionJpaRepository.save(solution);
  }

  public Solution getMySubmissionDetails(Long submissionId, String username) {
    Solution solution = solutionJpaRepository.findSolutionById(submissionId)
      .orElseThrow(SubmissionNotFoundException::new);
    if (!solution.getUserUsername().equals(username)) {
      throw new ForbiddenAccessException();
    }
    return solution;
  }

  public List<Solution> getAllMySubmissions(Long taskId, String username) {
    return solutionJpaRepository.findAllByUserUsernameIsAndTaskIdIs(username, taskId);
  }

  public List<Solution> getAllSubmissionDetailsOfTask(Long taskId) {
    return solutionJpaRepository.findAllByTaskId(taskId);
  }

  public byte[] getTaskSources(Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    return task.getSourceInZip();
  }

  public byte[] getTaskTestsSources(Long taskId) {
    Task task = taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
    return task.getTestSourceInZip();
  }

  public byte[] getMySubmissionSources(Long submissionId, String myUsername) {
    Solution solution = solutionJpaRepository.findSolutionById(submissionId)
      .orElseThrow(SubmissionNotFoundException::new);
    if (!solution.getUserUsername().equals(myUsername)) {
      throw new ForbiddenAccessException();
    }
    return solution.getSourceInZip();
  }

  public byte[] getSubmissionSources(Long submissionId) {
    Solution solution = solutionJpaRepository.findSolutionById(submissionId)
      .orElseThrow(SubmissionNotFoundException::new);
    return solution.getSourceInZip();
  }

  public int submissionsDoneOnTaskByUser(Long taskId, String username) {
    return solutionJpaRepository.countSubmissionsOnTaskByUser(taskId, username);
  }

  @AllArgsConstructor
  public static class SolutionCheckingResult {

    public FullReport fullReport;
    public int statusCode;
  }

}


