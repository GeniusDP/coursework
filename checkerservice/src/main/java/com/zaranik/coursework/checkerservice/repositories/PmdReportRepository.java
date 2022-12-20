package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.pmd.PmdReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PmdReportRepository extends JpaRepository<PmdReportEntity, Long> {

}
