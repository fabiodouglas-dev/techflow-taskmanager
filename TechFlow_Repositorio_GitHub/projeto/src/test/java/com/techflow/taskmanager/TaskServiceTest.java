package com.techflow.taskmanager;

import com.techflow.taskmanager.model.Task;
import com.techflow.taskmanager.repository.TaskRepository;
import com.techflow.taskmanager.service.TaskService;

/**
 * Testes unitários para o TaskService.
 * Valida as regras de negócio e operações CRUD do sistema.
 *
 * Executado automaticamente pelo GitHub Actions via pipeline CI/CD.
 */
public class TaskServiceTest {

    private TaskService service;
    private int passed = 0;
    private int failed = 0;

    /** Inicializa instâncias frescas antes de cada bloco de testes */
    private void setUp() {
        TaskRepository repository = new TaskRepository();
        service = new TaskService(repository);
    }

    // ==================== TESTES DE CRIAÇÃO ====================

    /** Teste: criar tarefa com dados válidos */
    void testCreateTask_ValidData() {
        setUp();
        Task task = service.createTask("Implementar login", "Tela de autenticação", "HIGH", "Ana");
        assertNotNull(task.getId(), "testCreateTask_ValidData: ID não deve ser nulo");
        assertEquals("Implementar login", task.getTitle(), "testCreateTask_ValidData: título incorreto");
        assertEquals("HIGH", task.getPriority(), "testCreateTask_ValidData: prioridade incorreta");
        assertEquals("TO_DO", task.getStatus(), "testCreateTask_ValidData: status inicial incorreto");
    }

    /** Teste: criar tarefa com título vazio deve lançar exceção */
    void testCreateTask_EmptyTitle_ThrowsException() {
        setUp();
        try {
            service.createTask("", "desc", "LOW", "João");
            fail("testCreateTask_EmptyTitle: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testCreateTask_EmptyTitle: exceção corretamente lançada");
        }
    }

    /** Teste: criar tarefa com título nulo deve lançar exceção */
    void testCreateTask_NullTitle_ThrowsException() {
        setUp();
        try {
            service.createTask(null, "desc", "MEDIUM", "Maria");
            fail("testCreateTask_NullTitle: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testCreateTask_NullTitle: exceção corretamente lançada");
        }
    }

    /** Teste: criar tarefa com prioridade inválida */
    void testCreateTask_InvalidPriority_ThrowsException() {
        setUp();
        try {
            service.createTask("Tarefa X", "desc", "URGENTE", "Carlos");
            fail("testCreateTask_InvalidPriority: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testCreateTask_InvalidPriority: exceção corretamente lançada");
        }
    }

    /** Teste: título com mais de 100 caracteres deve ser rejeitado */
    void testCreateTask_TitleTooLong_ThrowsException() {
        setUp();
        String longTitle = "A".repeat(101);
        try {
            service.createTask(longTitle, "desc", "LOW", "Ana");
            fail("testCreateTask_TitleTooLong: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testCreateTask_TitleTooLong: exceção corretamente lançada");
        }
    }

    // ==================== TESTES DE LEITURA ====================

    /** Teste: buscar tarefa existente por ID */
    void testGetTaskById_ExistingTask() {
        setUp();
        Task created = service.createTask("Tarefa A", "desc", "LOW", "Pedro");
        Task found = service.getTaskById(created.getId());
        assertEquals(created.getId(), found.getId(), "testGetTaskById: IDs devem ser iguais");
    }

    /** Teste: buscar tarefa inexistente deve lançar exceção */
    void testGetTaskById_NonExisting_ThrowsException() {
        setUp();
        try {
            service.getTaskById(999L);
            fail("testGetTaskById_NonExisting: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testGetTaskById_NonExisting: exceção corretamente lançada");
        }
    }

    /** Teste: listar todas as tarefas */
    void testGetAllTasks() {
        setUp();
        service.createTask("Tarefa 1", "", "LOW", "Ana");
        service.createTask("Tarefa 2", "", "HIGH", "João");
        assertEquals(2, service.getAllTasks().size(), "testGetAllTasks: deve retornar 2 tarefas");
    }

    // ==================== TESTES DE ATUALIZAÇÃO ====================

    /** Teste: atualizar status para IN_PROGRESS */
    void testUpdateTaskStatus_ToInProgress() {
        setUp();
        Task task = service.createTask("Tarefa Status", "", "MEDIUM", "Ana");
        Task updated = service.updateTaskStatus(task.getId(), "IN_PROGRESS");
        assertEquals("IN_PROGRESS", updated.getStatus(), "testUpdateStatus: status deve ser IN_PROGRESS");
    }

    /** Teste: ao marcar como DONE, data de conclusão deve ser definida */
    void testUpdateTaskStatus_ToDone_SetsCompletedAt() {
        setUp();
        Task task = service.createTask("Tarefa Done", "", "HIGH", "Maria");
        Task updated = service.updateTaskStatus(task.getId(), "DONE");
        assertEquals("DONE", updated.getStatus(), "testUpdateStatus_Done: status deve ser DONE");
        assertNotNull(updated.getCompletedAt(), "testUpdateStatus_Done: completedAt não deve ser nulo");
    }

    /** Teste: status inválido deve lançar exceção */
    void testUpdateTaskStatus_Invalid_ThrowsException() {
        setUp();
        Task task = service.createTask("Tarefa X", "", "LOW", "João");
        try {
            service.updateTaskStatus(task.getId(), "PENDENTE");
            fail("testUpdateStatus_Invalid: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testUpdateStatus_Invalid: exceção corretamente lançada");
        }
    }

    // ==================== TESTES DE EXCLUSÃO ====================

    /** Teste: deletar tarefa existente */
    void testDeleteTask_ExistingTask() {
        setUp();
        Task task = service.createTask("Para deletar", "", "LOW", "Ana");
        service.deleteTask(task.getId());
        assertEquals(0, service.getAllTasks().size(), "testDeleteTask: lista deve estar vazia");
    }

    /** Teste: deletar tarefa inexistente deve lançar exceção */
    void testDeleteTask_NonExisting_ThrowsException() {
        setUp();
        try {
            service.deleteTask(999L);
            fail("testDeleteTask_NonExisting: deveria lançar IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            pass("testDeleteTask_NonExisting: exceção corretamente lançada");
        }
    }

    // ==================== UTILITÁRIOS ====================

    private void assertNotNull(Object value, String message) {
        if (value == null) fail(message + " - valor não deveria ser nulo");
        else pass(message);
    }

    private void assertEquals(Object expected, Object actual, String message) {
        if (expected == null ? actual != null : !expected.equals(actual)) {
            fail(message + " - esperado: " + expected + ", obtido: " + actual);
        } else {
            pass(message);
        }
    }

    private void pass(String message) {
        System.out.println("[PASS] " + message);
        passed++;
    }

    private void fail(String message) {
        System.out.println("[FAIL] " + message);
        failed++;
    }

    /** Executa todos os testes e exibe o relatório final */
    public void runAll() {
        System.out.println("======================================");
        System.out.println("  Executando Testes - TechFlow Tasks");
        System.out.println("======================================\n");

        testCreateTask_ValidData();
        testCreateTask_EmptyTitle_ThrowsException();
        testCreateTask_NullTitle_ThrowsException();
        testCreateTask_InvalidPriority_ThrowsException();
        testCreateTask_TitleTooLong_ThrowsException();
        testGetTaskById_ExistingTask();
        testGetTaskById_NonExisting_ThrowsException();
        testGetAllTasks();
        testUpdateTaskStatus_ToInProgress();
        testUpdateTaskStatus_ToDone_SetsCompletedAt();
        testUpdateTaskStatus_Invalid_ThrowsException();
        testDeleteTask_ExistingTask();
        testDeleteTask_NonExisting_ThrowsException();

        System.out.println("\n======================================");
        System.out.printf("  Resultado: %d/%d testes passaram%n", passed, passed + failed);
        System.out.println("======================================");

        if (failed > 0) {
            System.exit(1); // Falha no CI/CD pipeline
        }
    }

    public static void main(String[] args) {
        new TaskServiceTest().runAll();
    }
}
