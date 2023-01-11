package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

  @Size(min = 3, max = 50, message = "username size should be from 3 to 50")
  private String username;

  @Size(max = 50, message = "email max size = 50")
  @Email(message = "email must be a valid email")
  private String email;

  @Size(min = 5, max = 50, message = "password size should be from 5 to 50")
  private String password;

  @Size(min = 1, max = 50, message = "firstName size should be from 1 to 50")
  private String firstName;

  @Size(min = 1, max = 50, message = "lastName size should be from 1 to 50")
  private String lastName;

}
