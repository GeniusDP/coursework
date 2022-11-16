package com.example.demo.controllers;

import com.example.demo.aspect.security.roles.admin.AdminGrant;
import com.example.demo.aspect.security.basic.SecuredRoute;
import com.example.demo.dto.ChangeRoleDto;
import com.example.demo.dto.RegistrationUserDto;
import com.example.demo.dto.ResponseStringWrapper;
import com.example.demo.dto.TokenContainingDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.entities.User;
import com.example.demo.services.UserManagementService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserManagementController {

  private final UserManagementService userManagementService;

  @GetMapping("/exists-by-username")
  public ResponseStringWrapper userExistsByUsername(@RequestParam String username) {
    userManagementService.userExistsByUsername(username);
    return ResponseStringWrapper.of("exists");
  }

  @PostMapping("/register")
  public void register(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
    userManagementService.register(registrationUserDto);
  }

  @PostMapping("/activate-account")
  public ResponseStringWrapper activateUserByLink() {
    throw new IllegalArgumentException("Feature not implemented yet");
  }

  @SecuredRoute
  @PutMapping("/update")
  public User updateUser(@Valid @RequestBody UpdateUserDto updateDto) {
    return userManagementService.updateUser(updateDto);
  }

  @AdminGrant
  @SecuredRoute
  @PutMapping("/users/{username}/change-role")
  public User makeTeacher(@PathVariable String username, @Valid @RequestBody ChangeRoleDto dto) {
    return userManagementService.changeRole(username, dto.getRoleValue());
  }

  @SecuredRoute
  @PostMapping("/users/{username}")
  public User getUserInfo(@PathVariable String username, @Valid @RequestBody TokenContainingDto dto){
    return userManagementService.getUserInfo(username);
  }


}
