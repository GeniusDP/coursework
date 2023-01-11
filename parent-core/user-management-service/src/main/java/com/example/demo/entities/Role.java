package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private RoleValue name;

  public Role(RoleValue name) {
    this.name = name;
  }

}
