package com.example.demo.services;

import com.example.demo.dto.RateLimitDto;
import com.example.demo.dto.RegistrationUserDto;
import com.example.demo.dto.TeacherGrantRequestDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.entities.Role;
import com.example.demo.entities.RoleValue;
import com.example.demo.entities.TeacherGrantRequest;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ForbiddenAccessException;
import com.example.demo.exceptions.RegistrationException;
import com.example.demo.exceptions.TeacherGrantRequestNotFoundException;
import com.example.demo.exceptions.TooManyRequestsException;
import com.example.demo.exceptions.UserDetailsUpdateException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.TeacherGrantRequestRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.hashingutility.HashingUtilityService;
import com.example.demo.utils.JwtTokenUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserManagementService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final HashingUtilityService hashingUtilityService;
  private final JwtTokenUtil jwtTokenUtil;
  private final RateLimitService rateLimitService;
  private final TeacherGrantRequestRepository teacherGrantRequestRepository;
  
  public boolean userExistsByUsername(String username) {
    boolean exists = userRepository.existsByUsername(username);
    if(!exists){
      throw new UserNotFoundException();
    }
    return true;
  }

  @Transactional
  public void register(RegistrationUserDto registrationUserDto) {
    String username = registrationUserDto.getUsername();
    String password = registrationUserDto.getPassword();
    String email = registrationUserDto.getEmail();
    String firstName = registrationUserDto.getFirstName();
    String lastName = registrationUserDto.getLastName();
    if (userRepository.existsByUsername(username)) {
      throw new RegistrationException("User with such username already exists");
    }

    Role userRole = roleRepository.findByName(RoleValue.STUDENT);
    String hashedPassword = hashingUtilityService.hash(password);
    User newUser = User.builder()
        .setUsername(username)
        .setPassword(hashedPassword)
        .setEmail(email)
        .setFirstName(firstName)
        .setLastName(lastName)
        .build();
    newUser.setRole(userRole);
    userRepository.save(newUser);
  }

  @Transactional
  public User updateUser(UpdateUserDto updateDto) {
    User user = userRepository.findByUsername(updateDto.getUsername());
    if(user == null){
      throw new UserDetailsUpdateException();
    }

    String newUsername = updateDto.getUsername();
    if(userRepository.existsByUsername(newUsername)){
      throw new UserDetailsUpdateException();
    }
    if(newUsername != null){
      user.setUsername(newUsername);
    }

    if(updateDto.getPassword() != null){
      user.setPassword(updateDto.getPassword());
    }

    if(updateDto.getEmail() != null){
      user.setEmail(updateDto.getEmail());
    }

    if(updateDto.getFirstName() != null){
      user.setFirstName(updateDto.getFirstName());
    }

    if(updateDto.getLastName() != null){
      user.setLastName(updateDto.getLastName());
    }

    userRepository.save(user);
    return user;
  }

  public User getUserInfo(String username, String accessToken) {
    String usernameFromToken = jwtTokenUtil.getUserNameFromToken(accessToken);
    if(!usernameFromToken.equals(username)){
      throw new ForbiddenAccessException();
    }
    User user = userRepository.findByUsername(username);
    if(user == null){
      throw new UserNotFoundException();
    }
    return user;
  }

  public User changeRole(String username, RoleValue roleValue) {
    User user = userRepository.findByUsername(username);
    if(user == null){
      throw new UserNotFoundException();
    }
    Role role = roleRepository.findByName(roleValue);
    user.setRole(role);
    userRepository.save(user);
    return user;
  }

  @Transactional
  public void requestTeacherGrant(String accessToken) {
    String username = jwtTokenUtil.getUserNameFromToken(accessToken);
    RateLimitDto rateLimitDto = rateLimitService.getRateLimitDto(username);
    if (!rateLimitDto.succeeded()) {
      throw new TooManyRequestsException(rateLimitDto);
    }
    TeacherGrantRequest grantRequest = new TeacherGrantRequest(username);
    teacherGrantRequestRepository.save(grantRequest);
  }

  public List<TeacherGrantRequestDto> getAllTeacherGrantRequests() {
    return teacherGrantRequestRepository.findAll().stream().map(TeacherGrantRequest::toDto).toList();
  }

  public void deleteTeacherGrantRequestByRequestId(Long requestId) {
    TeacherGrantRequest grantRequest = teacherGrantRequestRepository.findById(requestId)
      .orElseThrow(TeacherGrantRequestNotFoundException::new);
    teacherGrantRequestRepository.delete(grantRequest);
  }

  public void acceptTeacherGrantRequest(Long requestId) {
    TeacherGrantRequest grantRequest = teacherGrantRequestRepository.findById(requestId)
      .orElseThrow(TeacherGrantRequestNotFoundException::new);

    String username = grantRequest.getUsername();
    changeRole(username, RoleValue.TEACHER);
    teacherGrantRequestRepository.deleteAllByUsername(username);
  }
}
