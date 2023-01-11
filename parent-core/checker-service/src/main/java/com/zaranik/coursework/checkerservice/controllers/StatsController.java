package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.aspect.security.basic.SecuredRoute;
import com.zaranik.coursework.checkerservice.aspect.security.roles.teacher.TeacherGrant;
import com.zaranik.coursework.checkerservice.dtos.response.DoubleNumberValueDto;
import com.zaranik.coursework.checkerservice.dtos.response.LongNumberValueDto;
import com.zaranik.coursework.checkerservice.dtos.response.RuntimeStatusStatsDto;
import com.zaranik.coursework.checkerservice.dtos.response.TotalScoreDistributionStatsDto;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import com.zaranik.coursework.checkerservice.services.StatisticsService;
import com.zaranik.coursework.checkerservice.utils.JwtTokenUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checker-service")
public class StatsController {

  private final SolutionService solutionService;
  private final StatisticsService statisticsService;
  private final JwtTokenUtil jwtTokenUtil;

  @SecuredRoute
  @GetMapping("/my-submissions/{submissionId}")
  public Solution getMySubmissionOfTask(@PathVariable Long submissionId, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return solutionService.getMySubmissionDetails(submissionId, username);
  }

  @SecuredRoute
  @GetMapping("/tasks/{taskId}/my-submissions")
  public List<Solution> getAllMySubmissionsOfTask(@PathVariable Long taskId, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return solutionService.getAllMySubmissions(taskId, username);
  }

  @TeacherGrant
  @SecuredRoute
  @GetMapping("/tasks/{taskId}/submissions")
  public List<Solution> getAllSubmissionDetailsOfTask(@PathVariable Long taskId) {
    return solutionService.getAllSubmissionDetailsOfTask(taskId);
  }

  @TeacherGrant
  @SecuredRoute
  @GetMapping("/tasks/{taskId}/task-sources")
  public byte[] getTaskSources(@PathVariable Long taskId) {
    return solutionService.getTaskSources(taskId);
  }

  @TeacherGrant
  @SecuredRoute
  @GetMapping("/tasks/{taskId}/task-tests-sources")
  public byte[] getTaskTestSources(@PathVariable Long taskId) {
    return solutionService.getTaskTestsSources(taskId);
  }

  @SecuredRoute
  @GetMapping("/my-submissions/{submissionId}/sources")
  public byte[] getSolutionSources(@PathVariable Long submissionId, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
    String myUsername = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return solutionService.getMySubmissionSources(submissionId, myUsername);
  }

  @TeacherGrant
  @SecuredRoute
  @GetMapping("/submissions/{submissionId}/sources")
  public byte[] getSubmissionSources(@PathVariable Long submissionId) {
    return solutionService.getSubmissionSources(submissionId);
  }


  @GetMapping("/stats/task/{taskId}/runtime-statuses-in-groups")
  public List<RuntimeStatusStatsDto> countRuntimeStatusesGrouped(@PathVariable Long taskId){
    return statisticsService.countRuntimeStatusesGrouped(taskId);
  }


  @GetMapping("/stats/task/{taskId}/percentage-of-fully-correct-solutions")
  public DoubleNumberValueDto countPercentageOfFullyCorrectSolutions(@PathVariable Long taskId){
    return statisticsService.countPercentageOfFullyCorrectSolutions(taskId);
  }

  @GetMapping("/stats/task/{taskId}/distribution-of-totalscores")
  public TotalScoreDistributionStatsDto getDistributionOfTotalScores(@PathVariable Long taskId){
    return statisticsService.getDistributionOfTotalScores(taskId);
  }


  @GetMapping("/stats/task/{taskId}/average-totalscore")
  public DoubleNumberValueDto getAverageTotalScore(@PathVariable Long taskId){
    return statisticsService.getAverageTotalScore(taskId);
  }

  @GetMapping("/stats/task/{taskId}/number-of-submissions")
  public LongNumberValueDto countSubmissionsNumberOfTask(@PathVariable Long taskId){
    return statisticsService.countSubmissionsNumberOfTask(taskId);
  }

}
