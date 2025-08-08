package com.interview.taskmanager.domain.models;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate due_date;

    public Task() {
    }

    public Task(String id, String title, String description, TaskStatus status, LocalDate due_date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.due_date = due_date;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && status == task.status && Objects.equals(due_date, task.due_date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, due_date);
    }
}
