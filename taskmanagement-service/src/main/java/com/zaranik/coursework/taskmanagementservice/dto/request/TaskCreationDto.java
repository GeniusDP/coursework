package com.zaranik.coursework.taskmanagementservice.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskCreationDto {
  private String name;
  private String description;
  private MultipartFile sourceInZip;
  private MultipartFile testSourceInZip;
}
