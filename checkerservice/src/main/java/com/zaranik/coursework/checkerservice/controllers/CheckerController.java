package com.zaranik.coursework.checkerservice.controllers;

import com.zaranik.coursework.checkerservice.aspect.security.basic.SecuredRoute;
import com.zaranik.coursework.checkerservice.dtos.response.LongNumberValueDto;
import com.zaranik.coursework.checkerservice.entities.Solution;
import com.zaranik.coursework.checkerservice.services.CheckerService;
import com.zaranik.coursework.checkerservice.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
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

  private final CheckerService checkerService;
  private final JwtTokenUtil jwtTokenUtil;

  @SecuredRoute
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

  @SecuredRoute
  @GetMapping(path = "/tasks/{taskId}/submissions-left")
  public LongNumberValueDto getHowManySubmissionsLeft(
    @PathVariable("taskId") Long taskId,
    @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
  ) {
    String username = jwtTokenUtil.getUserNameFromToken(authorizationHeader.substring(7));
    return new LongNumberValueDto(checkerService.submissionsLeft(taskId, username), "number of submissions left");
  }


}
