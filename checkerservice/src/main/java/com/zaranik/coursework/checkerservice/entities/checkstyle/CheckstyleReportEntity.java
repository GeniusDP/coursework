package com.zaranik.coursework.checkerservice.entities.checkstyle;

import com.zaranik.coursework.checkerservice.entities.BaseEntity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "checkstyle_reports")
@NoArgsConstructor
@AllArgsConstructor
public class CheckstyleReportEntity extends BaseEntity {

  @OneToMany(mappedBy = "report")
  private List<CheckstyleSourceFile> checkstyleSourceFile;

}
