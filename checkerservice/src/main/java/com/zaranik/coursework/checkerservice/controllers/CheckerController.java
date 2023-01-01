package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.aspect.security.basic.SecuredRoute;
import com.zaranik.coursework.checkerservice.aspect.security.roles.teacher.TeacherGrant;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.services.CheckerService;
import com.zaranik.coursework.checkerservice.services.SolutionService;
import com.zaranik.coursework.checkerservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import java.util.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
  private final JwtTokenUtil jwtTokenUtil;

//  @SecuredRoute
  @PostMapping(path = "/tasks/{taskId}/check-solution", consumes = "multipart/form-data")
  public Solution checkSolution(
    @PathVariable("taskId") Long taskId,
    @RequestParam("file") MultipartFile solutionZip,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
  ) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    Solution solution = checkerService.registerSolution(taskId, solutionZip, username);
    return checkerService.checkSolution(solution);
  }

//  @SecuredRoute
  @GetMapping("/my-submissions/{submissionId}")
  public Solution getMySubmissionOfTask(@PathVariable Long submissionId, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return solutionService.getMySubmissionDetails(submissionId, username);
  }

//  @SecuredRoute
  @GetMapping("/tasks/{taskId}/my-submissions")
  public List<Solution> getAllMySubmissionsOfTask(@PathVariable Long taskId, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return solutionService.getAllMySubmissions(taskId, username);
  }

//  @TeacherGrant
//  @SecuredRoute
  @GetMapping("/tasks/{taskId}/submissions")
  public List<Solution> getAllSubmissionDetailsOfTask(@PathVariable Long taskId) {
    return solutionService.getAllSubmissionDetailsOfTask(taskId);
  }

//  @TeacherGrant
//  @SecuredRoute
  @GetMapping("/tasks/{taskId}/task-sources")
  public byte[] getTaskSources(@PathVariable Long taskId) {
    return solutionService.getTaskSources(taskId);
  }

//  @TeacherGrant
//  @SecuredRoute
  @GetMapping("/tasks/{taskId}/task-tests-sources")
  public byte[] getTaskTestSources(@PathVariable Long taskId) {
    return solutionService.getTaskTestsSources(taskId);
  }

//  @SecuredRoute
  @GetMapping("/my-submissions/{submissionId}/sources")
  public byte[] getSolutionSources(@PathVariable Long submissionId, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
    String myUsername = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return solutionService.getMySubmissionSources(submissionId, myUsername);
  }

//  @TeacherGrant
//  @SecuredRoute
  @GetMapping("/submissions/{submissionId}/sources")
  public byte[] getSubmissionSources(@PathVariable Long submissionId) {
    return solutionService.getSubmissionSources(submissionId);
  }

}
