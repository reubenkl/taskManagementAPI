package com.interview.taskmanager.application;

import com.interview.taskmanager.api.TaskRequestDTO;
import com.interview.taskmanager.api.TaskUpdateRequestDTO;
import com.interview.taskmanager.application.service.TaskService;
import com.interview.taskmanager.domain.models.Task;
import com.interview.taskmanager.domain.models.TaskStatus;
import com.interview.taskmanager.domain.repository.ITaskRepository;
import com.interview.taskmanager.infrastructure.Exception.TaskNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private ITaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1, task2, task3;
    private List<Task> allTasks;

    @BeforeEach
    void setUp() {
        task1 = new Task("1","Title 1","Description of Title 1", TaskStatus.PENDING, LocalDate.now().plusDays(3));
        task2 = new Task("2","Title 2","Description of Title 2", TaskStatus.IN_PROGRESS, LocalDate.now().plusDays(1));
        task3 = new Task("3","Title 3","Description of Title 3", TaskStatus.DONE, LocalDate.now().plusDays(2));
        allTasks = Arrays.asList(task1, task2, task3);
    }

    @Test
    void createTask_shouldSaveAndReturnTaskWithCorrectData() {
        //Creating a task request DTO
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Title 1", "Title 1 Description", null, LocalDate.now().plusDays(2));

        //Mock the Repository to return the given task
        when(taskRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //When we do the Service call
        Task result = taskService.createTask(taskRequestDTO);

        assertEquals(taskRequestDTO.getTitle(), result.getTitle());
        assertEquals(taskRequestDTO.getDescription(), result.getDescription());
        assertEquals(TaskStatus.PENDING, result.getStatus());
        verify(taskRepository).save(result);
    }

    @Test
    void getTask_shouldReturnTaskWhenFound() throws TaskNotFoundException {
        //Mock the repository to return the mock task data
        when(taskRepository.findById("1")).thenReturn(Optional.of(task1));

        //When we call service getTask method we should get the task
        Task result = taskService.getTask(task1.getId());

        assertEquals(task1, result);
    }

    @Test
    void getTask_shouldThrowTaskNotFoundExceptionWhenNotFound() {
        // Mock the repository to return the empty result
        when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

        // When we try to get the task by a non-existent ID then an exception should be thrown
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask("non-existent-id"));
    }

    @Test
    void updateTask_shouldUpdateTaskFields() throws TaskNotFoundException {
        // Giving the updated request
        TaskUpdateRequestDTO request = new TaskUpdateRequestDTO("Updated Title", null, TaskStatus.DONE, null);

        // Mocking repository to return the mock response
        when(taskRepository.findById(task1.getId())).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        Task updatedTask = taskService.updateTask(task1.getId(), request);

        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
    }

    @Test
    void updateTask_shouldThrowTaskNotFoundExceptionWhenNotFound() {
        // Mock the repository to return the empty result
        when(taskRepository.findById(anyString())).thenReturn(Optional.empty());

        // When we try to get the task by a non-existent ID then an exception should be thrown
        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask("non-existent-id", new TaskUpdateRequestDTO()));
    }

    @Test
    void deleteTask_shouldCallRepoDeleteMethodWhenTaskExists() throws TaskNotFoundException {
        when(taskRepository.findById(task1.getId())).thenReturn(Optional.of(task1));

        taskService.deleteTask(task1.getId());

        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void deleteTask_shouldThrowExceptionWhenNotFound() {
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask("non-existent-id"));
    }

    @Test
    void getAllTasks_shouldReturnAllTasksSortedByDueDate() {
        // Mocking a repository that returns a list of unsorted tasks
        when(taskRepository.findAll()).thenReturn(allTasks);

        // When we call the service method to get all tasks, then the service should return the tasks sorted by due date
        List<Task> result = taskService.getAllTasks(null, 0, 10);

        assertEquals(3, result.size());
        assertEquals(task2, result.get(0));
        assertEquals(task3, result.get(1));
        assertEquals(task1, result.get(2));
    }

    @Test
    void getAllTasks_shouldFilterAndPaginate() {
        // Mocking a repository to returns a list of PENDING tasks
        List<Task> pendingTasks = Arrays.asList(task2, task3);
        when(taskRepository.findAll(any(Predicate.class))).thenReturn(pendingTasks);

        // When we call the service method with filter and pagination
        List<Task> result = taskService.getAllTasks(TaskStatus.PENDING, 1, 1);

        // Then the service should return the correct paginated subset of the filtered list
        assertEquals(1, result.size());
        assertEquals(task3, result.get(0));
    }

}
