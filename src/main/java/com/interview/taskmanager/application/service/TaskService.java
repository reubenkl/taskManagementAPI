package com.interview.taskmanager.application.service;

import com.interview.taskmanager.api.TaskRequestDTO;
import com.interview.taskmanager.api.TaskUpdateRequestDTO;
import com.interview.taskmanager.domain.models.Task;
import com.interview.taskmanager.domain.models.TaskStatus;
import com.interview.taskmanager.domain.repository.ITaskRepository;
import com.interview.taskmanager.infrastructure.Exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Service
public class TaskService {

    private final ITaskRepository taskRepository;

    public TaskService(ITaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public Task createTask(TaskRequestDTO taskRequestDTO) {
        Task task = new Task(
                UUID.randomUUID().toString(),
                taskRequestDTO.getTitle(),
                taskRequestDTO.getDescription(),
                taskRequestDTO.getStatus(),
                taskRequestDTO.getDue_date()
        );
        return taskRepository.save(task);
    }

    public Task getTask(String id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    public Task updateTask(String id, TaskUpdateRequestDTO updatedRequest) throws TaskNotFoundException {
        Task existingTask = getTask(id);
        if(updatedRequest.getTitle() != null) {
            existingTask.setTitle(updatedRequest.getTitle());
        }
        if(updatedRequest.getDescription() != null) {
            existingTask.setDescription(updatedRequest.getDescription());
        }
        if(updatedRequest.getStatus() != null) {
            existingTask.setStatus(updatedRequest.getStatus());
        }
        if(updatedRequest.getDue_date() != null) {
            existingTask.setDue_date(updatedRequest.getDue_date());
        }
        return taskRepository.save(existingTask);
    }

    public void deleteTask(String id) throws TaskNotFoundException {
        getTask(id);
        taskRepository.deleteById(id);
    }

    public List<Task> getAllTasks(TaskStatus status, Integer page, Integer size) {
        List<Task> tasks;
        if (status != null) {
            Predicate<Task> filter = task -> task.getStatus().equals(status);
            tasks = taskRepository.findAll(filter);
        } else {
            tasks = taskRepository.findAll();
        }

        // Sort all tasks by due date
        tasks.sort(Comparator.comparing(Task::getDue_date));

        // Apply pagination
        int start = Math.min(page * size, tasks.size());
        int end = Math.min(start + size, tasks.size());

        return tasks.subList(start, end);
    }
}
