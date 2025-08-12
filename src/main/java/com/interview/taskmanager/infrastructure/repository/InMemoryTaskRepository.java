package com.interview.taskmanager.infrastructure.repository;

import com.interview.taskmanager.domain.models.Task;
import com.interview.taskmanager.domain.repository.ITaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryTaskRepository implements ITaskRepository {
    private final ConcurrentHashMap<String, Task> tasks = new ConcurrentHashMap<>();
    @Override
    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        return tasks.values().stream().collect(Collectors.toList());
    }

    @Override
    public List<Task> findAll(Predicate<Task> filter) {
        return tasks.values().stream().filter(filter).collect(Collectors.toList());
    }
}
