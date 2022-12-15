package com.example.solutionservice.repositories;

import com.example.solutionservice.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
