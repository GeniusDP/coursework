package com.zaranik.coursework.checkerservice.repositories;

import com.zaranik.coursework.checkerservice.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
