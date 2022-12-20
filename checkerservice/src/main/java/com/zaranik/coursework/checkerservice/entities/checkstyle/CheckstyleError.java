package com.zaranik.coursework.checkerservice.entities.checkstyle;

import com.zaranik.coursework.checkerservice.entities.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "checkstyle_errors")
@NoArgsConstructor
@AllArgsConstructor
public class CheckstyleError extends BaseEntity {

  @Column(name = "line")
  private Integer line;

  @Column(name = "message")
  private String message;

  @Column(name = "source")
  private String source;

  @ManyToOne
  @JoinColumn(name = "checkstyle_file_id")
  private CheckstyleSourceFile file;

}
