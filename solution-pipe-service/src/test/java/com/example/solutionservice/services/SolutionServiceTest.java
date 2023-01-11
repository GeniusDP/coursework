package com.example.solutionservice.services;

import static org.mockito.ArgumentMatchers.any;

import com.example.solutionservice.dto.response.SubmissionDto;
import com.example.solutionservice.dto.response.TaskSourcesDto;
import com.example.solutionservice.entities.Solution;
import com.example.solutionservice.entities.Task;
import com.example.solutionservice.exceptions.SolutionNotFoundException;
import com.example.solutionservice.exceptions.TaskNotFoundException;
import com.example.solutionservice.repositories.SubmissionRepository;
import com.example.solutionservice.repositories.TaskRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SolutionServiceTest {

  private SolutionService solutionService;

  @BeforeEach
  public void initEach() throws IOException, URISyntaxException {

    byte[] taskZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    byte[] testZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("test.zip").toURI()));

    Task task = Task.builder()
      .name("Task 1")
      .description("Description for task 1")
      .testPoints(70)
      .checkstylePoints(15)
      .pmdPoints(15)
      .checkstyleNeeded(true)
      .pmdNeeded(true)
      .sourceInZip(taskZip)
      .testSourceInZip(testZip)
      .build();

    task.setId(1L);

    List<Task> tasks = new ArrayList<>(){{
      add(task);
    }};

    TaskRepository taskRepository = Mockito.mock(TaskRepository.class);

    Mockito.doAnswer(invocation -> {
      Long id = invocation.getArgument(0, Long.class);
      return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }).when(taskRepository).findById(any(Long.class));

    Mockito.doAnswer(invocation -> {
      Long id = invocation.getArgument(0, Task.class).getId();
      tasks.removeIf(t -> t.getId().equals(id));
      return tasks;
    }).when(taskRepository).delete(any(Task.class));

    Mockito.doAnswer(invocation -> {
      Task taskToSave = invocation.getArgument(0, Task.class);
      if(taskToSave.getId() == null) {
        long nextId = 1;
        Optional<Task> max = tasks.stream().max((t1, t2) -> (int) (t1.getId() - t2.getId()));
        if(max.isPresent()){
          nextId = max.get().getId() + 1;
        }
        taskToSave.setId(nextId);
      } else {
        tasks.removeIf(t -> t.getId().equals(taskToSave.getId()));
      }
      tasks.add(taskToSave);
      return taskToSave;
    }).when(taskRepository).save(any(Task.class));

    Mockito.doReturn(tasks).when(taskRepository).findAll();

    SubmissionRepository submissionRepository = Mockito.mock(SubmissionRepository.class);

    byte[] solutionZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("full-score-solution.zip").toURI()));

    Solution solution = Solution.builder()
      .sourceInZip(solutionZip)
      .userUsername("student")
      .task(task)
      .build();
    solution.setId(1L);
    List<Solution> solutions = new ArrayList<>();
    solutions.add(solution);


    Mockito.doAnswer(invocation -> {
      Long id = invocation.getArgument(0, Long.class);
      return solutions.stream().filter(s -> s.getId().equals(id)).findFirst();
    }).when(submissionRepository).findById(any(Long.class));

    solutionService = new SolutionService(submissionRepository, taskRepository);
  }

  @Test
  void getSubmissionById() {
    SubmissionDto submissionById = solutionService.getSubmissionById(1L);
    Assertions.assertThat(submissionById).isNotNull();
    org.junit.jupiter.api.Assertions.assertThrows(
      SolutionNotFoundException.class,
      () -> solutionService.getSubmissionById(100L)
    );

  }

  @Test
  void getTaskSourcesById() throws IOException, URISyntaxException {
    byte[] expectedTaskZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    byte[] expectedTestZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("test.zip").toURI()));

    TaskSourcesDto taskSourcesById = solutionService.getTaskSourcesById(1L);
    Assertions.assertThat(taskSourcesById).isNotNull();
    byte[] sourceInZipBytes = taskSourcesById.sourceInZip();
    byte[] testSourceInZipBytes = taskSourcesById.testSourceInZip();

    Assertions.assertThat(sourceInZipBytes).isEqualTo(expectedTaskZip);
    Assertions.assertThat(testSourceInZipBytes).isEqualTo(expectedTestZip);

    org.junit.jupiter.api.Assertions.assertThrows(
      TaskNotFoundException.class,
      () -> solutionService.getTaskSourcesById(100L)
    );
  }

}
