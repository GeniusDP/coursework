package com.zaranik.coursework.taskmanagementservice.repositories;

import com.zaranik.coursework.taskmanagementservice.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
