package com.interview.taskmanager.api;

import com.interview.taskmanager.domain.models.Task;
import com.interview.taskmanager.domain.models.TaskStatus;

import java.time.LocalDate;

public class TaskResponseDTO {

    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate due_date;

    public TaskResponseDTO() {
    }

    public TaskResponseDTO(String id, String title, String description, TaskStatus status, LocalDate due_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.due_date = due_date;
    }

    public static TaskResponseDTO convertTaskToTaskResponseDTO(Task task){
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDue_date()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }
}
