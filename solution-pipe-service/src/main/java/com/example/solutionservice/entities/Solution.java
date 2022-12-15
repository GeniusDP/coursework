package com.example.solutionservice.entities;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "submissions")
@NoArgsConstructor
@AllArgsConstructor
public class Solution {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "source_in_zip", nullable = false)
  private byte[] sourceInZip;

  @Column(name = "compilation_status")
  private String compilationStatus;

  @Column(name = "tests_number")
  private Integer testsNumber;

  @Column(name = "tests_passed")
  private Integer testsPassed;

  @Column(name = "testing_status")
  private String testingStatus;

  @Column(name = "user_id")
  private Long userId;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;

  public Solution(byte[] sourceInZip) {
    this.sourceInZip = sourceInZip;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Solution solution = (Solution) o;
    return Objects.equals(id, solution.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
