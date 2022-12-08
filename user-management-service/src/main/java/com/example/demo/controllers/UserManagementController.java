package com.example.demo.controllers;

import com.example.demo.aspect.security.roles.admin.AdminGrant;
import com.example.demo.aspect.security.basic.SecuredRoute;
import com.example.demo.dto.RegistrationUserDto;
import com.example.demo.dto.ResponseStringWrapper;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.entities.RoleValue;
import com.example.demo.entities.User;
import com.example.demo.services.UserManagementService;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

  @AdminGrant
  @SecuredRoute
  @PatchMapping("/users/{username}/change-role")
  public User makeTeacher(@PathVariable String username, @NotNull RoleValue role) {
    return userManagementService.changeRole(username, role);
  }

  @SecuredRoute
  @GetMapping("/users/{username}")
  public User getUserInfo(@PathVariable String username, @RequestHeader("Authorization") @Pattern(regexp = "^Bearer\s.*$") String accessToken){
    return userManagementService.getUserInfo(username, accessToken);
  }


}
