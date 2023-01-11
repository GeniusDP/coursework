package com.example.demo.dto;

import com.example.demo.entities.RoleValue;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleDto {

  @NotNull
  private RoleValue roleValue;

}
