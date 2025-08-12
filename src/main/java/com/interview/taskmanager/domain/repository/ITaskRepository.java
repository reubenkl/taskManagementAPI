package com.interview.taskmanager.domain.repository;

import com.interview.taskmanager.domain.models.Task;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ITaskRepository {
    Task save(Task task);

    Optional<Task> findById(String id);

    List<Task> findAll();

    List<Task> findAll(Predicate<Task> filter);
}