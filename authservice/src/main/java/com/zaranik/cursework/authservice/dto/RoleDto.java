package com.zaranik.cursework.authservice.dto;

import com.zaranik.cursework.authservice.entities.RoleValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDto {

  private RoleValue role;

  public RoleDto(RoleValue userRole) {
    this.role = userRole;
  }
}
