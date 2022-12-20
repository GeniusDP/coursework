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

  @Column(name = "pmd_needed", nullable = false)
  private boolean pmdNeeded;

  @Column(name = "checkstyle_needed", nullable = false)
  private boolean checkstyleNeeded;

  @Column(name = "pmd_points", nullable = false)
  private int pmdPoints;

  @Column(name = "checkstyle_points", nullable = false)
  private int checkstylePoints;

  @Column(name = "test_points", nullable = false)
  private int testPoints;

}
