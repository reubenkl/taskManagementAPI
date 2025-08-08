package com.interview.taskmanager.infrastructure.controller;

import com.interview.taskmanager.api.TaskRequestDTO;
import com.interview.taskmanager.api.TaskResponseDTO;
import com.interview.taskmanager.api.TaskUpdateRequestDTO;
import com.interview.taskmanager.application.service.TaskService;
import com.interview.taskmanager.domain.models.Task;
import com.interview.taskmanager.domain.models.TaskStatus;
import com.interview.taskmanager.infrastructure.Exception.TaskNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        Task createdTask = taskService.createTask(taskRequestDTO);
        return new ResponseEntity<>(TaskResponseDTO.convertTaskToTaskResponseDTO(createdTask), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable String id) throws Exception {
        Task retrievedTask = taskService.getTask(id);
        return new ResponseEntity<>(TaskResponseDTO.convertTaskToTaskResponseDTO(retrievedTask), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable String id, @Valid @RequestBody TaskUpdateRequestDTO taskUpdateRequestDTO) throws Exception{
        Task updatedTask = taskService.updateTask(id, taskUpdateRequestDTO);
        return new ResponseEntity<>(TaskResponseDTO.convertTaskToTaskResponseDTO(updatedTask), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String id) throws Exception {
        taskService.deleteTask(id);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> listAllTasks(@RequestParam(required = false) TaskStatus status, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "3") Integer size) {
        List<Task> tasks = taskService.getAllTasks(status, page, size);
        List<TaskResponseDTO> responseList = tasks.stream()
                .map(TaskResponseDTO::convertTaskToTaskResponseDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }
}
