package com.techflow.taskmanager.service;

import com.techflow.taskmanager.model.Task;
import com.techflow.taskmanager.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private final TaskRepository repository;

    private static final List<String> VALID_STATUSES = List.of("TO_DO", "IN_PROGRESS", "DONE");
    private static final List<String> VALID_PRIORITIES = List.of("LOW", "MEDIUM", "HIGH", "CRITICAL");

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task createTask(String title, String description, String priority, String assignee) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio.");
        }
        if (title.trim().length() > 100) {
            throw new IllegalArgumentException("O título não pode ter mais de 100 caracteres.");
        }
        for (Task existingTask : repository.findAll()) {
            if (existingTask.getTitle().equalsIgnoreCase(title.trim())) {
                throw new IllegalArgumentException("Já existe uma tarefa com o título: " + title.trim());
            }
        }
        if (priority != null && !VALID_PRIORITIES.contains(priority.toUpperCase())) {
            throw new IllegalArgumentException("Prioridade inválida: " + priority);
        }
        Task task = new Task();
        task.setTitle(title.trim());
        task.setDescription(description != null ? description.trim() : "");
        task.setPriority(priority != null ? priority.toUpperCase() : "MEDIUM");
        task.setAssignee(assignee != null ? assignee.trim() : "");
        return repository.save(task);
    }

    public Task getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com ID: " + id));
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public List<Task> getTasksByStatus(String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        return repository.findByStatus(status.toUpperCase());
    }

    public List<Task> getTasksByAssignee(String assignee) {
        if (assignee == null || assignee.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do responsável não pode ser vazio.");
        }
        List<Task> result = new ArrayList<>();
        for (Task task : repository.findAll()) {
            if (task.getAssignee().equalsIgnoreCase(assignee.trim())) {
                result.add(task);
            }
        }
        return result;
    }

    public Task updateTask(Long id, String title, String description, String priority, String assignee) {
        Task task = getTaskById(id);
        if (title != null && !title.trim().isEmpty()) {
            if (title.trim().length() > 100) {
                throw new IllegalArgumentException("O título não pode ter mais de 100 caracteres.");
            }
            task.setTitle(title.trim());
        }
        if (description != null) {
            task.setDescription(description.trim());
        }
        if (priority != null) {
            if (!VALID_PRIORITIES.contains(priority.toUpperCase())) {
                throw new IllegalArgumentException("Prioridade inválida: " + priority);
            }
            task.setPriority(priority.toUpperCase());
        }
        if (assignee != null) {
            task.setAssignee(assignee.trim());
        }
        return repository.save(task);
    }

    public Task updateTaskStatus(Long id, String newStatus) {
        if (!VALID_STATUSES.contains(newStatus.toUpperCase())) {
            throw new IllegalArgumentException("Status inválido: " + newStatus);
        }
        Task task = getTaskById(id);
        task.setStatus(newStatus.toUpperCase());
        if ("DONE".equalsIgnoreCase(newStatus)) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }
        return repository.save(task);
    }

    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    public String getStats() {
        int total = repository.count();
        int todo = repository.findByStatus("TO_DO").size();
        int inProgress = repository.findByStatus("IN_PROGRESS").size();
        int done = repository.findByStatus("DONE").size();
        return String.format(
            "Total: %d | A Fazer: %d | Em Progresso: %d | Concluídas: %d",
            total, todo, inProgress, done
        );
    }
}
