package com.zaranik.coursework.checkerservice.entities.checkstyle;

import com.zaranik.coursework.checkerservice.entities.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@Table(name = "checkstyle_reports")
@AllArgsConstructor
public class CheckstyleReport extends BaseEntity {

}
