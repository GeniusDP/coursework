package com.example.demo.controllers;

import com.example.demo.aspect.security.basic.SecuredRoute;
import com.example.demo.aspect.security.roles.admin.AdminGrant;
import com.example.demo.dto.RegistrationUserDto;
import com.example.demo.dto.ResponseStringWrapper;
import com.example.demo.dto.TeacherGrantRequestDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.entities.User;
import com.example.demo.services.UserManagementService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/api/user-management")
@RequiredArgsConstructor
public class UserManagementController {

  private final UserManagementService userManagementService;

  @GetMapping("/exists-by-username")
  public ResponseStringWrapper userExistsByUsername(@RequestParam @NotNull String username) {
    userManagementService.userExistsByUsername(username);
    return ResponseStringWrapper.of("exists");
  }

  @PostMapping("/register")
  public void register(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
    userManagementService.register(registrationUserDto);
  }

  @SecuredRoute
  @PatchMapping("/update")
  public User updateUser(@Valid @RequestBody UpdateUserDto updateDto) {
    return userManagementService.updateUser(updateDto);
  }

  @SecuredRoute
  @GetMapping("/users/{username}")
  public User getUserInfo(@PathVariable String username, @RequestHeader("Authorization") @Pattern(regexp = "^Bearer\s.*$") String accessToken){
    return userManagementService.getUserInfo(username, accessToken);
  }


  //teacher grant controllers
  @SecuredRoute
  @PostMapping("/requests-for-teacher-grant/request")
  public void requestTeacherGrant(@RequestHeader("Authorization") @Pattern(regexp = "^Bearer\s.*$") String accessToken) {
    userManagementService.requestTeacherGrant(accessToken);
  }

  @AdminGrant
  @SecuredRoute
  @GetMapping("/requests-for-teacher-grant/get-all")
  public List<TeacherGrantRequestDto> getAllRequests() {
    return userManagementService.getAllTeacherGrantRequests();
  }

  @AdminGrant
  @SecuredRoute
  @PostMapping("/requests-for-teacher-grant/accept/{requestId}")
  public void acceptTeacherGrantRequest(@PathVariable Long requestId) {
    userManagementService.acceptTeacherGrantRequest(requestId);
  }

  @AdminGrant
  @SecuredRoute
  @PostMapping("/requests-for-teacher-grant/reject/{requestId}")
  public void rejectTeacherGrantRequest(@PathVariable Long requestId) {
    userManagementService.deleteTeacherGrantRequestByRequestId(requestId);
  }

}
