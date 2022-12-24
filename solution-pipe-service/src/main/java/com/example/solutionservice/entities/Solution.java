package com.example.solutionservice.entities;

import com.example.solutionservice.entities.checkstyle.CheckstyleReport;
import com.example.solutionservice.entities.pmd.PmdReport;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Solution extends BaseEntity {

  @Column(name = "source_in_zip", nullable = false)
  private byte[] sourceInZip;

  @Column(name = "compilation_status")
  private String compilationStatus;

  @Column(name = "tests_run")
  private Integer testsRun;

  @Column(name = "tests_passed")
  private Integer testsPassed;

  @Column(name = "testing_status")
  private String testingStatus;

  @Column(name = "user_username")
  private String userUsername;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;

  @OneToOne
  @JoinColumn(name = "checkstyle_report_id")
  private CheckstyleReport checkstyleReport;

  @OneToOne
  @JoinColumn(name = "pmd_report_id")
  private PmdReport pmdReport;

  public Solution(byte[] sourceInZip) {
    this.sourceInZip = sourceInZip;
    this.testingStatus = "N/A";
    this.compilationStatus = "N/A";
    this.testsRun = -1;
    this.testsPassed = -1;
  }

  @Override
  public String toString() {
    return "Solution{" +
      "id=" + id +
      ", compilationStatus='" + compilationStatus + '\'' +
      ", testingStatus='" + testingStatus + '\'' +
      '}';
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
