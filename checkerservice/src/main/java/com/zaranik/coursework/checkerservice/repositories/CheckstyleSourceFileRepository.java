package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.checkstyle.CheckstyleSourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckstyleSourceFileRepository extends JpaRepository<CheckstyleSourceFile, Long> {

}
