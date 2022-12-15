package com.zaranik.coursework.taskmanagementservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "public_sources_in_zip", nullable = false)
  private byte[] sourceInZip;

  @Column(name = "private_test_sources_in_zip", nullable = false)
  private byte[] testSourceInZip;

  public Task(String name, String description, byte[] sourceInZip, byte[] testSourceInZip) {
    this.name = name;
    this.description = description;
    this.sourceInZip = sourceInZip;
    this.testSourceInZip = testSourceInZip;
  }
}
