package com.interview.taskmanager.api;

import com.interview.taskmanager.domain.models.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TaskRequestDTO {
        @NotBlank(message = "Title is mandatory")
        private String title;
        private String description;
        private TaskStatus status = TaskStatus.PENDING;
        @NotNull(message = "Due date is mandatory")
        @FutureOrPresent(message = "Due date must be in the present or future")
        private LocalDate due_date;

    public TaskRequestDTO() {
    }

    public TaskRequestDTO(String title, String description, TaskStatus status, LocalDate due_date) {
        this.title = title;
        this.description = description;
        if(status != null) {
            this.status = status;
        }
        this.due_date = due_date;
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
        if(status != null) {
            this.status = status;
        }
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }
}
