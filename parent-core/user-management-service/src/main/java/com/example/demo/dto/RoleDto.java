package com.example.demo.dto;

import com.example.demo.entities.RoleValue;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

  @NotNull(message = "role must not be null")
  private RoleValue role;

  public RoleDto(RoleValue userRole) {
    this.role = userRole;
  }
}
