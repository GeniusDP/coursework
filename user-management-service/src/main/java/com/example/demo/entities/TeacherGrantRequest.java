package com.example.demo.entities;

import com.example.demo.dto.TeacherGrantRequestDto;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "teacher_grant_requests")
@NoArgsConstructor
public class TeacherGrantRequest extends BaseEntity {

  private String username;

  public TeacherGrantRequest(String username) {
    this.username = username;
  }

  public TeacherGrantRequestDto toDto() {
    return new TeacherGrantRequestDto(id, username);
  }

}
