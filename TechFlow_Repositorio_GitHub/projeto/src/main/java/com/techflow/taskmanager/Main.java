package com.techflow.taskmanager;

import com.techflow.taskmanager.controller.TaskController;
import com.techflow.taskmanager.repository.TaskRepository;
import com.techflow.taskmanager.service.TaskService;

/**
 * Classe principal do sistema TechFlow Task Manager.
 * Inicializa os componentes e inicia a aplicação.
 *
 * Projeto: TechFlow Solutions - Gerenciador de Tarefas Ágil
 * Disciplina: Engenharia de Software - UniFECAF
 * Metodologia: Kanban / Scrum híbrido
 */
public class Main {

    public static void main(String[] args) {
        // Inicialização do repositório (camada de dados)
        TaskRepository repository = new TaskRepository();

        // Inicialização do serviço (camada de negócio)
        TaskService service = new TaskService(repository);

        // Inicialização do controlador (camada de apresentação)
        TaskController controller = new TaskController(service);

        // Início da aplicação
        controller.start();
    }
}
