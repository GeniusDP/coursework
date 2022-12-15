package com.zaranik.coursework.taskmanagementservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponseDto {
  private Long id;
  private String name;
}
