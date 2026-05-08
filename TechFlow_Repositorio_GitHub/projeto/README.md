#TechFlow Task Manager

> Sistema de Gerenciamento de Tarefas Ágil — TechFlow Solutions  
> Projeto Acadêmico · Engenharia de Software · UniFECAF

![Java](https://img.shields.io/badge/Java-17-orange?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.9-red?logo=apachemaven)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-blue?logo=github-actions)
![Metodologia](https://img.shields.io/badge/Metodologia-Kanban%2FScrum-green)

---

##Objetivo do Projeto

A **TechFlow Solutions** foi contratada por uma startup de logística para desenvolver um sistema de gerenciamento de tarefas baseado em metodologias ágeis. O sistema permite acompanhar o fluxo de trabalho em tempo real, priorizar tarefas críticas e monitorar o desempenho da equipe.

---

##Escopo Inicial

O sistema contempla as seguintes funcionalidades principais:

- **CRUD completo de tarefas** — criar, listar, atualizar e excluir
- **Gerenciamento de status** — A Fazer → Em Progresso → Concluído
- **Priorização de tarefas** — LOW, MEDIUM, HIGH, CRITICAL
- **Atribuição de responsáveis** por tarefa
- **Registro automático de datas** de criação e conclusão
- **Filtros** por status e prioridade
- **Painel de estatísticas** do fluxo de trabalho
- **Testes automatizados** com cobertura das regras de negócio
- **Pipeline CI/CD** com GitHub Actions

---

##Arquitetura do Sistema

O projeto segue a arquitetura em três camadas (MVC simplificado):

```
src/
├── main/java/com/techflow/taskmanager/
│   ├── Main.java                     # Ponto de entrada da aplicação
│   ├── model/
│   │   └── Task.java                 # Entidade de Tarefa
│   ├── repository/
│   │   └── TaskRepository.java       # Camada de dados (in-memory)
│   ├── service/
│   │   └── TaskService.java          # Regras de negócio
│   └── controller/
│       └── TaskController.java       # Interface de linha de comando (CLI)
├── test/java/com/techflow/taskmanager/
│   └── TaskServiceTest.java          # Testes unitários
docs/
│   ├── diagrama-casos-uso.md         # Diagrama UML de Casos de Uso
│   └── diagrama-classes.md           # Diagrama UML de Classes
.github/workflows/
│   └── ci.yml                        # Pipeline GitHub Actions
```

---

##Metodologia Ágil Adotada

Este projeto utiliza uma abordagem **híbrida Kanban/Scrum**:

| Prática | Implementação |
|--------|---------------|
| **Kanban** | Quadro GitHub Projects com colunas To Do / In Progress / Done |
| **Sprints** | Ciclos de 1 semana por funcionalidade |
| **Backlog** | Issues do GitHub priorizadas por etiquetas |
| **CI/CD** | GitHub Actions executando testes a cada commit |
| **Commits Semânticos** | Padrão feat/fix/test/docs/refactor |

---

##Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.9+

### Clonar e Compilar
```bash
git clone https://github.com/seu-usuario/techflow-taskmanager.git
cd techflow-taskmanager
mvn compile
```

### Executar a Aplicação
```bash
mvn exec:java -Dexec.mainClass="com.techflow.taskmanager.Main"
```

### Executar os Testes
```bash
mvn test
```

### Gerar JAR Executável
```bash
mvn package
java -jar target/techflow-taskmanager.jar
```

---

##Testes Automatizados

Os testes cobrem todas as operações CRUD e as regras de negócio:

| Teste | Cenário |
|-------|---------|
| `testCreateTask_ValidData` | Criação com dados válidos |
| `testCreateTask_EmptyTitle` | Rejeição de título vazio |
| `testCreateTask_InvalidPriority` | Rejeição de prioridade inválida |
| `testCreateTask_TitleTooLong` | Título com mais de 100 chars |
| `testGetTaskById_ExistingTask` | Busca por ID existente |
| `testGetTaskById_NonExisting` | Exceção ao buscar ID inexistente |
| `testUpdateStatus_ToDone` | Preenchimento automático de completedAt |
| `testUpdateStatus_Invalid` | Rejeição de status inválido |
| `testDeleteTask_ExistingTask` | Exclusão bem-sucedida |
| `testDeleteTask_NonExisting` | Exceção ao deletar ID inexistente |

---

##Gestão de Mudanças — Alteração de Escopo

### Mudança Registrada: Sprint 3

**Data:** Semana 3 do desenvolvimento  
**Solicitante:** Cliente (Startup de Logística)

**Escopo Original:** Sistema CLI de gerenciamento de tarefas com CRUD básico.

**Alteração Solicitada:** O cliente solicitou a adição de um **sistema de notificações por e-mail** ao marcar tarefas como concluídas, e um **relatório exportável em CSV** das tarefas por período.

**Justificativa:** A startup identificou que os gestores precisam de visibilidade fora do sistema para coordenar com parceiros externos. A funcionalidade de relatório CSV é essencial para reuniões semanais.

**Impacto:** Adicionados 2 novos cards no Kanban, estimativa de +3 dias de desenvolvimento. Nenhuma funcionalidade existente foi removida.

**Status:** Implementado na branch `feature/notifications-and-export`.

---

##GitHub Projects — Kanban

O quadro Kanban está disponível na aba **Projects** deste repositório.

🔗 [Acessar quadro Kanban](https://github.com/fabiodouglas-dev/techflow-taskmanager/projects)

Organização atual das tarefas:
- **To Do:** 3 tarefas planejadas
- **In Progress:** 2 tarefas em andamento
- **Done:** 7 tarefas concluídas

---

##Equipe

| Nome | Papel |
|Fabio |Desenvolvedor|
| Desenvolvedor | Arquitetura, Backend, Testes |
| TechFlow Solutions | Cliente fictício |

---

## Referências

- Pressman, R. — *Engenharia de Software: Uma Abordagem Profissional*
- [GitHub Docs — Actions](https://docs.github.com/en/actions)
- [Atlassian — Kanban Guide](https://www.atlassian.com/agile/kanban)
