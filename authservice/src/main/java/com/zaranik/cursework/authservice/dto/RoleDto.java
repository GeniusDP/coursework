package com.zaranik.cursework.authservice.dto;

import com.zaranik.cursework.authservice.entities.RoleValue;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {

    private RoleValue role;

    public RoleDto(RoleValue userRole) {
        this.role = userRole;
    }
}
