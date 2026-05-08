package com.techflow.taskmanager.controller;

import com.techflow.taskmanager.model.Task;
import com.techflow.taskmanager.service.TaskService;

import java.util.List;
import java.util.Scanner;

/**
 * Controlador responsável pela interface de linha de comando (CLI).
 * Recebe entradas do usuário e delega operações ao TaskService.
 */
public class TaskController {

    private final TaskService taskService;
    private final Scanner scanner;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Inicia o loop principal do menu interativo.
     */
    public void start() {
        System.out.println("========================================");
        System.out.println("  TechFlow - Gerenciador de Tarefas");
        System.out.println("  Sistema de Gestão Ágil v1.0");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createTask();
                case "2" -> listAllTasks();
                case "3" -> updateTask();
                case "4" -> updateStatus();
                case "5" -> deleteTask();
                case "6" -> filterByStatus();
                case "7" -> showStats();
                case "0" -> {
                    System.out.println("\nEncerrando o sistema. Até logo!");
                    running = false;
                }
                default -> System.out.println("\n[ERRO] Opção inválida. Tente novamente.");
            }
        }
    }
/** Exibe o menu principal */
private void printMenu() {
    System.out.println("\n╔══════════════════════════════╗");
    System.out.println("║       MENU PRINCIPAL         ║");
    System.out.println("╠══════════════════════════════╣");
    System.out.println("║  1. Criar nova tarefa        ║");
    System.out.println("║  2. Listar todas as tarefas  ║");
    System.out.println("║  3. Atualizar tarefa         ║");
    System.out.println("║  4. Atualizar status         ║");
    System.out.println("║  5. Excluir tarefa           ║");
    System.out.println("║  6. Filtrar por status       ║");
    System.out.println("║  7. Estatísticas             ║");
    System.out.println("║  0. Sair                     ║");
    System.out.println("╚══════════════════════════════╝");
    System.out.print("\nEscolha uma opção: ");
}
    /** Fluxo de criação de tarefa */
    private void createTask() {
        System.out.println("\n--- CRIAR NOVA TAREFA ---");

        System.out.print("Título: ");
        String title = scanner.nextLine();

        System.out.print("Descrição: ");
        String description = scanner.nextLine();

        System.out.print("Prioridade (LOW/MEDIUM/HIGH/CRITICAL) [padrão: MEDIUM]: ");
        String priority = scanner.nextLine();
        if (priority.isEmpty()) priority = "MEDIUM";

        System.out.print("Responsável: ");
        String assignee = scanner.nextLine();

        try {
            Task created = taskService.createTask(title, description, priority, assignee);
            System.out.println("\n[OK] Tarefa criada com sucesso! ID: " + created.getId());
            printTask(created);
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    /** Lista todas as tarefas */
    private void listAllTasks() {
        System.out.println("\n--- TODAS AS TAREFAS ---");
        List<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }

        tasks.forEach(this::printTask);
    }

    /** Fluxo de atualização de dados da tarefa */
    private void updateTask() {
        System.out.println("\n--- ATUALIZAR TAREFA ---");
        System.out.print("ID da tarefa: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Novo título (deixe vazio para manter): ");
            String title = scanner.nextLine();

            System.out.print("Nova descrição (deixe vazio para manter): ");
            String description = scanner.nextLine();

            System.out.print("Nova prioridade (deixe vazio para manter): ");
            String priority = scanner.nextLine();

            System.out.print("Novo responsável (deixe vazio para manter): ");
            String assignee = scanner.nextLine();

            Task updated = taskService.updateTask(
                id,
                title.isEmpty() ? null : title,
                description.isEmpty() ? null : description,
                priority.isEmpty() ? null : priority,
                assignee.isEmpty() ? null : assignee
            );

            System.out.println("\n[OK] Tarefa atualizada com sucesso!");
            printTask(updated);

        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] ID inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    /** Fluxo de atualização de status */
    private void updateStatus() {
        System.out.println("\n--- ATUALIZAR STATUS ---");
        System.out.print("ID da tarefa: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Novo status (TO_DO/IN_PROGRESS/DONE): ");
            String status = scanner.nextLine();

            Task updated = taskService.updateTaskStatus(id, status);
            System.out.println("\n[OK] Status atualizado para: " + updated.getStatus());
            printTask(updated);

        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] ID inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    /** Fluxo de exclusão de tarefa */
    private void deleteTask() {
        System.out.println("\n--- EXCLUIR TAREFA ---");
        System.out.print("ID da tarefa: ");

        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            taskService.deleteTask(id);
            System.out.println("\n[OK] Tarefa removida com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] ID inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    /** Filtra tarefas por status */
    private void filterByStatus() {
        System.out.println("\n--- FILTRAR POR STATUS ---");
        System.out.print("Status (TO_DO/IN_PROGRESS/DONE): ");

        try {
            String status = scanner.nextLine();
            List<Task> tasks = taskService.getTasksByStatus(status);

            if (tasks.isEmpty()) {
                System.out.println("Nenhuma tarefa com status: " + status);
                return;
            }

            System.out.println("\nTarefas com status '" + status.toUpperCase() + "':");
            tasks.forEach(this::printTask);

        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERRO] " + e.getMessage());
        }
    }

    /** Exibe estatísticas gerais */
    private void showStats() {
        System.out.println("\n--- ESTATÍSTICAS ---");
        System.out.println(taskService.getStats());
    }

    /** Formata e exibe uma tarefa no console */
    private void printTask(Task task) {
        System.out.println("------------------------------------------");
        System.out.printf("ID: %d | %s [%s] [%s]%n",
            task.getId(), task.getTitle(), task.getStatus(), task.getPriority());
        if (!task.getDescription().isEmpty()) {
            System.out.println("Descrição: " + task.getDescription());
        }
        if (!task.getAssignee().isEmpty()) {
            System.out.println("Responsável: " + task.getAssignee());
        }
        System.out.println("Criado em: " + task.getCreatedAt());
        if (task.getCompletedAt() != null) {
            System.out.println("Concluído em: " + task.getCompletedAt());
        }
    }
}
