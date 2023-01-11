package com.zaranik.coursework.taskmanagementservice.services;

import static org.mockito.ArgumentMatchers.any;

import com.zaranik.coursework.taskmanagementservice.dto.request.TaskCreationDto;
import com.zaranik.coursework.taskmanagementservice.dto.response.TaskResponseDto;
import com.zaranik.coursework.taskmanagementservice.entities.Task;
import com.zaranik.coursework.taskmanagementservice.exceptions.ForbiddenAccessException;
import com.zaranik.coursework.taskmanagementservice.exceptions.TaskCreationFailedException;
import com.zaranik.coursework.taskmanagementservice.exceptions.TaskNotFoundException;
import com.zaranik.coursework.taskmanagementservice.repositories.TaskRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

  private TaskService taskService;

  @BeforeEach
  public void initEach() throws IOException, URISyntaxException {

    byte[] taskZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    Task task1 = Task.builder()
      .name("Task 1")
      .description("Description for task 1")
      .creatorName("teacher")
      .testPoints(70)
      .checkstylePoints(15)
      .pmdPoints(15)
      .checkstyleNeeded(true)
      .pmdNeeded(true)
      .submissionsNumberLimit(-1)
      .sourceInZip(taskZip)
      .solutionList(Collections.emptyList())
      .build();

    Task task2 = Task.builder()
      .name("Task 2")
      .description("Description for task 2")
      .creatorName("teacher")
      .testPoints(70)
      .checkstylePoints(15)
      .pmdPoints(15)
      .checkstyleNeeded(true)
      .pmdNeeded(true)
      .submissionsNumberLimit(10)
      .solutionList(Collections.emptyList())
      .build();
    Task task3 = Task.builder()
      .name("Task 3")
      .description("Description for task 3")
      .creatorName("teacher_second")
      .testPoints(100)
      .checkstylePoints(0)
      .pmdPoints(0)
      .checkstyleNeeded(false)
      .pmdNeeded(false)
      .submissionsNumberLimit(3)
      .solutionList(Collections.emptyList())
      .build();

    task1.setId(1L);
    task2.setId(2L);
    task3.setId(3L);

    List<Task> tasks = new ArrayList<>(){{
      addAll(List.of(task1, task2, task3));
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

    taskService = new TaskService(taskRepository);
  }

  @Test
  void getAllTasks() {
    List<TaskResponseDto> tasks = taskService.getAllTasks();
    Assertions.assertThat(tasks.size()).isEqualTo(3);
  }

  @Test
  void createNewTask_normalCase() throws IOException, URISyntaxException {
    byte[] fileZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    TaskCreationDto dto = TaskCreationDto.builder()
      .name("New task")
      .description("Description for new task")
      .testPoints(100)
      .checkstylePoints(0)
      .pmdPoints(0)
      .checkstyleNeeded(false)
      .pmdNeeded(false)
      .sourceInZip(new MockMultipartFile("task.zip", fileZip))
      .testSourceInZip(new MockMultipartFile("test.zip", fileZip))
      .submissionsNumberLimit(3)
      .build();
    String creatorName = "teacher_second";
    TaskResponseDto newTask = taskService.createNewTask(dto, creatorName);
    List<TaskResponseDto> tasks = taskService.getAllTasks();
    Assertions.assertThat(tasks.size()).isEqualTo(4);
    Assertions.assertThat(newTask.getSubmissionsNumberLimit()).isEqualTo(3);

  }

  @Test
  void createNewTask_SumIsNot100() throws IOException, URISyntaxException {
    byte[] fileZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    TaskCreationDto dto = TaskCreationDto.builder()
      .name("New task")
      .description("Description for new task")
      .testPoints(90)
      .checkstylePoints(10)
      .pmdPoints(10)
      .checkstyleNeeded(false)
      .pmdNeeded(false)
      .sourceInZip(new MockMultipartFile("task.zip", fileZip))
      .testSourceInZip(new MockMultipartFile("test.zip", fileZip))
      .submissionsNumberLimit(3)
      .build();
    String creatorName = "teacher_second";
    org.junit.jupiter.api.Assertions.assertThrows(
      TaskCreationFailedException.class, () -> taskService.createNewTask(dto, creatorName));
  }

  @Test
  void createNewTask_ScoresDoesNotCorrespondTo() throws IOException, URISyntaxException {
    byte[] fileZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    TaskCreationDto dto = TaskCreationDto.builder()
      .name("New task")
      .description("Description for new task")

      .testPoints(80)

      .pmdPoints(10)
      .pmdNeeded(true)

      .checkstylePoints(10)
      .checkstyleNeeded(false)

      .sourceInZip(new MockMultipartFile("task.zip", fileZip))
      .testSourceInZip(new MockMultipartFile("test.zip", fileZip))
      .submissionsNumberLimit(3)
      .build();
    String creatorName = "teacher_second";
    org.junit.jupiter.api.Assertions.assertThrows(
      TaskCreationFailedException.class, () -> taskService.createNewTask(dto, creatorName));
  }

  @Test
  void createNewTask_CheckIfSubmissionsLimitEstablishedNullSetsMinusOneToDB() throws IOException, URISyntaxException {
    byte[] fileZip = Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("task.zip").toURI()));
    TaskCreationDto dto = TaskCreationDto.builder()
      .name("New task")
      .description("Description for new task")
      .testPoints(80)
      .pmdPoints(10)
      .pmdNeeded(true)
      .checkstylePoints(10)
      .checkstyleNeeded(true)
      .sourceInZip(new MockMultipartFile("task.zip", fileZip))
      .testSourceInZip(new MockMultipartFile("test.zip", fileZip))
      .submissionsNumberLimit(null)
      .build();
    String creatorName = "teacher_second";
    TaskResponseDto newTask = taskService.createNewTask(dto, creatorName);
    List<TaskResponseDto> tasks = taskService.getAllTasks();
    Assertions.assertThat(tasks.size()).isEqualTo(4);
    Assertions.assertThat(newTask.getSubmissionsNumberLimit()).isEqualTo(-1);
  }


  @Test
  void getTaskById() {
    TaskResponseDto dto = taskService.getTaskById(1L);
    Assertions.assertThat(dto.getName()).isEqualTo("Task 1");
    org.junit.jupiter.api.Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(100L));
  }

  @Test
  void getTaskSources() throws IOException {
    byte[] taskZipExpected = Files.readAllBytes(Path.of("src", "test", "resources", "task.zip"));
    byte[] taskZipProvided = taskService.getTaskSources(1L);
    Assertions.assertThat(taskZipProvided).isEqualTo(taskZipExpected);
  }

  @Test
  void deleteOwnTask() {
    List<TaskResponseDto> tasks = taskService.getAllTasks();
    Assertions.assertThat(tasks.size()).isEqualTo(3);

    taskService.deleteOwnTask(1L, "teacher");

    tasks = taskService.getAllTasks();
    Assertions.assertThat(tasks.size()).isEqualTo(2);

    org.junit.jupiter.api.Assertions.assertThrows(ForbiddenAccessException.class, () -> {
      taskService.deleteOwnTask(2L, "some_another_teacher");
    });
  }
}
