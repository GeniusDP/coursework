package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckstyleReportRepository extends JpaRepository<CheckstyleReportEntity, Long> {

}
