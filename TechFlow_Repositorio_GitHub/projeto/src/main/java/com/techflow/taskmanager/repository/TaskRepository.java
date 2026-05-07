package com.techflow.taskmanager.repository;

import com.techflow.taskmanager.model.Task;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória para armazenamento e acesso às tarefas.
 * Simula um banco de dados usando HashMap para operações CRUD.
 */
public class TaskRepository {

    // Mapa para armazenar tarefas: chave = ID, valor = Task
    private final Map<Long, Task> taskStore = new HashMap<>();

    // Gerador de IDs auto-incremento thread-safe
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Salva uma nova tarefa ou atualiza uma existente.
     * Se a tarefa não tiver ID, gera um novo automaticamente.
     */
    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        taskStore.put(task.getId(), task);
        return task;
    }

    /**
     * Busca uma tarefa pelo seu ID.
     * Retorna Optional vazio se não encontrar.
     */
    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(taskStore.get(id));
    }

    /**
     * Retorna todas as tarefas cadastradas.
     */
    public List<Task> findAll() {
        return new ArrayList<>(taskStore.values());
    }

    /**
     * Busca tarefas por status (TO_DO, IN_PROGRESS, DONE).
     */
    public List<Task> findByStatus(String status) {
        List<Task> result = new ArrayList<>();
        for (Task task : taskStore.values()) {
            if (status.equalsIgnoreCase(task.getStatus())) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * Busca tarefas por prioridade.
     */
    public List<Task> findByPriority(String priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : taskStore.values()) {
            if (priority.equalsIgnoreCase(task.getPriority())) {
                result.add(task);
            }
        }
        return result;
    }

    /**
     * Remove uma tarefa pelo ID.
     * Retorna true se removida com sucesso, false se não encontrada.
     */
    public boolean deleteById(Long id) {
        return taskStore.remove(id) != null;
    }

    /**
     * Verifica se uma tarefa existe pelo ID.
     */
    public boolean existsById(Long id) {
        return taskStore.containsKey(id);
    }

    /**
     * Retorna o número total de tarefas.
     */
    public int count() {
        return taskStore.size();
    }
}
