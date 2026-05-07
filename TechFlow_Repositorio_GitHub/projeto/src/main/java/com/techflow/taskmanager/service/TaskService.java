package com.techflow.taskmanager.service;

import com.techflow.taskmanager.model.Task;
import com.techflow.taskmanager.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócio do sistema de tarefas.
 * Implementa validações e regras de negócio antes de persistir dados.
 */
public class TaskService {

    private final TaskRepository repository;

    // Statuses e prioridades válidos
    private static final List<String> VALID_STATUSES = List.of("TO_DO", "IN_PROGRESS", "DONE");
    private static final List<String> VALID_PRIORITIES = List.of("LOW", "MEDIUM", "HIGH", "CRITICAL");

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria uma nova tarefa após validações.
     * Lança IllegalArgumentException se dados inválidos.
     */
    public Task createTask(String title, String description, String priority, String assignee) {
        // Validação do título (obrigatório e não vazio)
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("O título da tarefa não pode ser vazio.");
        }

        // Validação do tamanho do título (máximo 100 caracteres)
        if (title.trim().length() > 100) {
            throw new IllegalArgumentException("O título não pode ter mais de 100 caracteres.");
        }

        // Validação da prioridade
        if (priority != null && !VALID_PRIORITIES.contains(priority.toUpperCase())) {
            throw new IllegalArgumentException("Prioridade inválida: " + priority +
                    ". Válidas: " + VALID_PRIORITIES);
        }

        Task task = new Task();
        task.setTitle(title.trim());
        task.setDescription(description != null ? description.trim() : "");
        task.setPriority(priority != null ? priority.toUpperCase() : "MEDIUM");
        task.setAssignee(assignee != null ? assignee.trim() : "");

        return repository.save(task);
    }

    /**
     * Busca tarefa por ID. Lança exceção se não encontrada.
     */
    public Task getTaskById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tarefa não encontrada com ID: " + id));
    }

    /**
     * Retorna todas as tarefas do sistema.
     */
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    /**
     * Retorna tarefas filtradas por status.
     */
    public List<Task> getTasksByStatus(String status) {
        if (!VALID_STATUSES.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
        return repository.findByStatus(status.toUpperCase());
    }

    /**
     * Atualiza os dados de uma tarefa existente.
     */
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

    /**
     * Atualiza apenas o status de uma tarefa.
     * Se status = DONE, registra data de conclusão automaticamente.
     */
    public Task updateTaskStatus(Long id, String newStatus) {
        if (!VALID_STATUSES.contains(newStatus.toUpperCase())) {
            throw new IllegalArgumentException("Status inválido: " + newStatus);
        }

        Task task = getTaskById(id);
        task.setStatus(newStatus.toUpperCase());

        // Registra data de conclusão ao marcar como DONE
        if ("DONE".equalsIgnoreCase(newStatus)) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }

        return repository.save(task);
    }

    /**
     * Remove uma tarefa do sistema.
     */
    public void deleteTask(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Tarefa não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Retorna estatísticas básicas do sistema.
     */
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
