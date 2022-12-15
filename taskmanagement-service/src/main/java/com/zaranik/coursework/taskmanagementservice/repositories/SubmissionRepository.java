package com.zaranik.coursework.taskmanagementservice.repositories;

import com.zaranik.coursework.taskmanagementservice.entities.Submission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

  @Query("select s from Submission s where s.task.id = :taskId")
  List<Submission> getAllSubmissionsOfTask(Long taskId);

}
