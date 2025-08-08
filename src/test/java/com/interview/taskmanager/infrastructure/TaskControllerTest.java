package com.interview.taskmanager.infrastructure;

import com.interview.taskmanager.api.TaskResponseDTO;
import com.interview.taskmanager.api.TaskUpdateRequestDTO;
import com.interview.taskmanager.application.service.TaskService;
import com.interview.taskmanager.domain.models.Task;
import com.interview.taskmanager.domain.models.TaskStatus;
import com.interview.taskmanager.infrastructure.Exception.TaskNotFoundException;
import com.interview.taskmanager.infrastructure.controller.TaskController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    private Task task1, task2, task3;
    private List<Task> allTasks;

    @BeforeEach
    void setUp() {
        task1 = new Task("1","Title 1","Description of Title 1", TaskStatus.PENDING, LocalDate.now().plusDays(3));
        task2 = new Task("2","Title 2","Description of Title 2", TaskStatus.IN_PROGRESS, LocalDate.now().plusDays(4));
        task3 = new Task("3","Title 3","Description of Title 3", TaskStatus.DONE, LocalDate.now().plusDays(5));
        allTasks = Arrays.asList(task1, task2, task3);
    }


    @Test
    void createTask_shouldReturnCreatedTaskWithId() throws Exception {
        //request Payload
        String jsonPayload = """
                {
                  "title": "Title 1",
                  "description": "Description of Title 1",
                  "status": "PENDING",
                  "due_date": "2025-08-11"
                }
                """;
        //Mock Task data
        Task mockedTask = new Task();
        mockedTask.setId("1234");
        mockedTask.setTitle("Title");

        //Mocking service layer to return valid task
        when(taskService.createTask(any())).thenReturn(mockedTask);

        //When performed POST request then we expect 201 created status and valid JSON response
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(mockedTask.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(mockedTask.getTitle()));
    }

    @Test
    void getTask_shouldReturnTaskWhenFound() throws Exception {
        //Task which exists in the memory
        Task mockedTask = new Task();
        mockedTask.setId("1234");
        mockedTask.setTitle("Title");

        //Mocking the getTask method to retrun mock task
        when(taskService.getTask("1234")).thenReturn(mockedTask);

        //When we perform GET request for the task Id, we expect 200 OK Status and JSON response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/{id}", "1234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1234"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Title"));
    }

    @Test
    void getTask_shouldReturnTaskNotFoundWhenTaskDoesNotExist() throws Exception {
        String nonExistentId = "non-existent-id";

        // Mock the service to throw the expected exception
        when(taskService.getTask(nonExistentId)).thenThrow(new TaskNotFoundException("Task not found"));

        // WHEN we perform a GET request for a non-existent ID, then we expect a 404 Not Found status
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks/{id}", nonExistentId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception {
        //Mock Updated task
        Task updatedTask = new Task();
        updatedTask.setTitle(task1.getTitle());
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(task1.getStatus());
        updatedTask.setDue_date(LocalDate.now().plusDays(8));

        //request Payload
        String jsonPayload = """
                {
                  "title": "Title 1",
                  "description": "Updated Description",
                  "status": "PENDING",
                  "due_date": "2025-08-15"
                }
                """;

        //mock the service call with the mock data
        when(taskService.updateTask(any(), any(TaskUpdateRequestDTO.class))).thenReturn(updatedTask);

        //When we perform PUT request from an
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/{id}", task1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(updatedTask.getDescription()));
    }

    @Test
    void updateTask_shouldReturnTaskNotFoundWhenTaskDoesNotExist() throws Exception {
        String nonExistentId = "non-existent-id";

        // Mock the service to throw the expected exception
        when(taskService.updateTask(anyString(), any(TaskUpdateRequestDTO.class))).thenThrow(new TaskNotFoundException("Task not found"));

        // WHEN we perform a PUT request for a non-existent ID, then we expect a 404 Not Found status
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteTask_shouldReturn204StatusCode() throws Exception {
        // Mock the service call with the existing Id to do nothing on successful deletion
        doNothing().when(taskService).deleteTask(task1.getId());

        // When we call the DELETE api, then we expect 204 No Content Status response
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{id}", task1.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the service's deleteTask method was called exactly once
        verify(taskService, times(1)).deleteTask(task1.getId());
    }

    @Test
    void deleteTask_shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        // Given a non-existing Id
        String nonExistentId = "non-existent-id";

        // We mock the service to throw a TaskNotFoundException
        doThrow(new TaskNotFoundException("Task not found")).when(taskService).deleteTask(nonExistentId);

        // WHEN we perform a DELETE request for a non-existent ID, then we expect a 404 Not Found status
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/tasks/{id}", nonExistentId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void listAllTasks_shouldReturnSortedTasks() throws Exception{
        // Mocking the service to return the list of All tasks
        when(taskService.getAllTasks(nullable(TaskStatus.class), anyInt(), anyInt())).thenReturn(allTasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Title 2"));
    }

}
