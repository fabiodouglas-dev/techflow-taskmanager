package com.techflow.taskmanager.model;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma Tarefa no sistema.
 * Contém todos os atributos necessários para o gerenciamento de tarefas.
 */
public class Task {

    // Identificador único da tarefa
    private Long id;

    // Título da tarefa
    private String title;

    // Descrição detalhada da tarefa
    private String description;

    // Status: TO_DO, IN_PROGRESS, DONE
    private String status;

    // Prioridade: LOW, MEDIUM, HIGH, CRITICAL
    private String priority;

    // Responsável pela tarefa
    private String assignee;

    // Data de criação
    private LocalDateTime createdAt;

    // Data de conclusão (pode ser nula)
    private LocalDateTime completedAt;

    // Construtor padrão
    public Task() {
        this.createdAt = LocalDateTime.now();
        this.status = "TO_DO";
        this.priority = "MEDIUM";
    }

    // Construtor completo
    public Task(Long id, String title, String description, String priority, String assignee) {
        this();
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.assignee = assignee;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = assignee; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

   @Override
public String toString() {
    return "========== TAREFA ==========\n" +
           "ID:          " + id + "\n" +
           "Título:      " + title + "\n" +
           "Status:      " + status + "\n" +
           "Prioridade:  " + priority + "\n" +
           "Responsável: " + assignee + "\n" +
           "Criado em:   " + createdAt + "\n" +
           "============================";
    }
}
