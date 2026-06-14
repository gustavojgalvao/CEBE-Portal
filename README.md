# CEBE Portal — Fullstack

Portal do Aluno do CEBE (Centro de Educação e Bem-Estar), desenvolvido como monorepo com separação clara entre front-end e back-end.

## Estrutura do Projeto

```
CEBE-Portal/
├── client/          ← Front-end (HTML, CSS, JavaScript vanilla)
├── server/          ← Back-end (Java 21 + Spring Boot 3.5)
└── docs/            ← Documentação técnica e guias
```

---

## Front-end (`/client`)

Interface web construída com **HTML, CSS e JavaScript vanilla**, sem frameworks.

```
client/
├── public/               ← Arquivos estáticos (logo, imagens)
└── src/
    ├── assets/           ← Estilos CSS por página
    │   ├── style.css         — Design tokens globais e reset
    │   ├── dashboard.css     — Layout sidebar/topbar/bottom-nav
    │   ├── cursos.css        — Página de cursos
    │   ├── atendimento.css   — Página de atendimento/chat
    │   ├── notificacoes.css  — Página de notificações
    │   ├── financeiro.css    — Página financeira
    │   └── landing.css       — Página inicial pública
    ├── components/
    │   └── nav.js            — Injeta sidebar, topbar e bottom-nav em todas as páginas
    ├── pages/            ← Páginas HTML do aluno
    │   ├── login.html        — Autenticação do aluno (CPF + data de nascimento)
    │   ├── dashboard.html    — Painel principal
    │   ├── cursos.html       — Meus cursos, notas e frequência
    │   ├── matricula.html    — Inscrição em novas turmas
    │   ├── atendimento.html  — Chamados de suporte com chat em tempo real (SSE)
    │   ├── notificacoes.html — Central de notificações
    │   ├── financeiro.html   — Situação financeira e boletos
    │   └── admin/            ← Páginas do painel administrativo
    │       ├── login.html        — Autenticação do administrador
    │       ├── dashboard.html    — Painel do admin
    │       └── chat.html         — Chat admin para responder chamados
    └── services/         ← Módulos JavaScript de integração com a API
        ├── api.js            — Função central apiFetch() com tratamento de token e erros
        ├── auth.js           — login(), cadastrar(), logout()
        ├── auth-guard.js     — Redireciona para login se não houver token
        ├── notifications.js  — NotificationService: localStorage + polling + SSE sync
        ├── cursos.js         — listarCursos(), listarTurmas()
        ├── matriculas.js     — Funções de matrícula
        └── atendimento.js    — Funções de chamados de suporte
```

### Serviços JavaScript

#### `api.js` — Base de todas as requisições
- Endereço do servidor: `http://localhost:8080`
- Token JWT lido de `localStorage` com chave `cebe_token`
- Envia cabeçalho `Authorization: Bearer <token>`
- Redireciona para `login.html` em respostas `401` e `403`

#### `notifications.js` — NotificationService
- Armazena notificações em `localStorage` com chave `cebe_notificacoes`
- Emite o evento `notifications-updated` sempre que o estado muda
- Sincroniza com o backend via `GET /notificacoes/me`
- Polling automático a cada 30 segundos (`startPolling(30000)`)
- Sincroniza múltiplas abas via `localStorage` evento `cebe_notif_sync`

---

## Back-end (`/server`)

API REST construída com **Java 21 + Spring Boot 3.5**, autenticação **JWT** e banco de dados **MySQL**.

```
server/src/main/java/com/cebe/portal_aluno/
├── PortalAlunoApplication.java   ← Entry point (@SpringBootApplication)
├── config/
│   ├── SecurityConfig.java       ← Regras de autorização por rota + BCrypt + CORS
│   ├── SecurityFilter.java       ← Filtro JWT: valida token a cada request
│   └── DataSeeder.java           ← Cria admin padrão (admin@cebe.com / admin123) na 1ª inicialização
├── controller/
│   ├── AuthController.java           — POST /auth/login
│   ├── AdminAuthController.java      — POST /auth/admin/login
│   ├── AlunoController.java          — CRUD /alunos + GET /alunos/me
│   ├── CursosController.java         — CRUD /cursos
│   ├── TurmaController.java          — CRUD /turmas
│   ├── ProfessorController.java      — CRUD /professores
│   ├── MatriculaController.java      — CRUD /matriculas + GET /matriculas/me
│   ├── AtendimentoController.java    — CRUD /atendimentos + SSE /atendimentos/{id}/stream
│   ├── AdminAtendimentoController.java — Admin: listagem e resposta de chamados
│   └── NotificacaoController.java    — GET /notificacoes/me, PUT /ler, DELETE
├── service/
│   ├── AlunoService.java         ← CRUD de alunos, senha = data de nascimento (BCrypt)
│   ├── AuthorizationService.java ← UserDetailsService: busca aluno por CPF no login
│   ├── AtendimentoService.java   ← Abre chamado + cria notificação automática
│   ├── CursosService.java        ← CRUD de cursos
│   ├── MatriculaService.java     ← Valida vagas, evita duplicata, cria notificação
│   ├── NotificacaoService.java   ← Cria notificações para o aluno
│   ├── ProfessorService.java     ← CRUD de professores
│   ├── SseService.java           ← Server-Sent Events: chat em tempo real do atendimento
│   ├── TokenService.java         ← Geração e validação de JWT (HMAC256, expira em 2h)
│   └── TurmaService.java         ← CRUD de turmas
├── entity/
│   ├── Aluno.java            ← Tabela `aluno` — implements UserDetails (login por CPF)
│   ├── Admin.java            ← Tabela `admin` — implements UserDetails (login por email)
│   ├── Cursos.java           ← Tabela `cursos` (nome, cargaHoraria, bannerUrl)
│   ├── Professor.java        ← Tabela `professor`
│   ├── Turma.java            ← Tabela `turma` (cursos, professor, turno, vagas)
│   ├── Matricula.java        ← Tabela `matricula` (aluno, turma, statusPagamento)
│   ├── Atendimento.java      ← Tabela `atendimento` (chamado de suporte)
│   ├── MensagemAtendimento.java ← Tabela `mensagem_atendimento` (chat)
│   ├── Notificacao.java      ← Tabela `notificacao`
│   └── enums/
│       ├── StatusPagamento.java  — Pago | Pendente | Vencido
│       ├── StatusAtendimento.java — Finalizado | Pendente | Em_andamento
│       └── Turno.java            — Matutino | Vespertino
├── dto/
│   ├── request/   ← DTOs de entrada (Records Java)
│   └── response/  ← DTOs de saída
├── repository/    ← Interfaces Spring Data JPA
│   ├── AlunoRepository, AdminRepository, CursosRepository
│   ├── TurmaRepository, MatriculaRepository, ProfessorRepository
│   ├── AtendimentoRepository, MensagemAtendimentoRepository
│   └── NotificacaoRepository
└── exception/
    ├── GlobalExceptionHandler.java       ← Trata exceções globalmente com @RestControllerAdvice
    ├── RecursoNaoEncontradoException.java ← 404 Not Found
    ├── RegraDeNegocioException.java       ← 422 regra de negócio violada
    ├── ErrorResponseDTO.java
    └── FieldErrorDTO.java
```

### Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 3.5 | Framework web |
| Spring Data JPA + Hibernate | Persistência ORM (`ddl-auto=update`) |
| Spring Security 6 + JWT (Auth0) | Autenticação stateless |
| Server-Sent Events (SSE) | Chat em tempo real no atendimento |
| Lombok | Redução de boilerplate (`@Getter`, `@Builder`, etc.) |
| MySQL | Banco de dados (`localhost:3306/cebe`) |
| BCryptPasswordEncoder | Hash de senhas |

---

## Endpoints da API

### Autenticação
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| POST | `/auth/login` | Login do aluno (CPF + dataNascimento) | ❌ |
| POST | `/auth/admin/login` | Login do administrador (email + senha) | ❌ |

### Alunos
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| POST | `/alunos` | Cadastrar novo aluno | ❌ |
| GET | `/alunos/me` | Dados do aluno logado | ✅ Aluno |
| GET | `/alunos` | Listar todos | ✅ |
| PUT | `/alunos/{id}` | Atualizar aluno | ✅ |
| DELETE | `/alunos/{id}` | Remover aluno | ✅ |

### Matrículas
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| GET | `/matriculas/me` | Matrículas do aluno logado | ✅ Aluno |
| POST | `/matriculas` | Criar matrícula (`{ idAluno, idTurma }`) | ✅ |
| GET | `/matriculas` | Listar todas | ✅ |
| PUT | `/matriculas/{id}` | Atualizar matrícula | ✅ |
| DELETE | `/matriculas/{id}` | Remover matrícula | ✅ |

### Notificações
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| GET | `/notificacoes/me` | Notificações do aluno logado (ordem desc) | ✅ Aluno |
| PUT | `/notificacoes/{id}/ler` | Marcar notificação como lida | ✅ Aluno |
| PUT | `/notificacoes/ler-todas` | Marcar todas como lidas | ✅ Aluno |
| DELETE | `/notificacoes/{id}` | Remover notificação | ✅ Aluno |

### Atendimentos (Chamados)
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| GET | `/atendimentos` | Listar chamados do aluno logado | ✅ Aluno |
| POST | `/atendimentos` | Abrir novo chamado | ✅ Aluno |
| GET | `/atendimentos/{id}/stream` | Stream SSE do chat | token via query param |
| DELETE | `/atendimentos/{id}` | Remover chamado | ✅ |

### Admin — Atendimentos
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| GET | `/admin/atendimentos` | Todos os chamados | ✅ Admin |
| PUT | `/admin/atendimentos/{id}/status` | Atualizar status do chamado | ✅ Admin |
| POST | `/admin/atendimentos/{id}/mensagem` | Enviar mensagem no chat | ✅ Admin |
| GET | `/admin/atendimentos/{id}/stream` | Stream SSE admin | token via query param |

### Cursos, Turmas, Professores
| Método | Rota | Descrição |
|---|---|---|
| GET | `/cursos` | Listar cursos |
| POST/PUT/DELETE | `/cursos/{id}` | CRUD de cursos |
| GET | `/turmas` | Listar turmas |
| GET | `/professores` | Listar professores |

---

## Fluxo de Autenticação

```
1. Aluno envia CPF + dataNascimento → POST /auth/login
2. Spring Security chama AuthorizationService.loadUserByUsername(cpf)
3. Aluno encontrado → senha (dataNascimento hasheada) comparada com BCrypt
4. Login OK → TokenService.generateToken(aluno) → JWT (HMAC256, expira em 2h)
5. Token salvo no front em localStorage['cebe_token']
6. Requests subsequentes: header Authorization: Bearer <token>
7. SecurityFilter valida o token, extrai email e role → autentica no SecurityContext
```

---

## Chat em Tempo Real (SSE)

O atendimento usa **Server-Sent Events** (não WebSocket):

- Aluno/Admin abre conexão com `GET /atendimentos/{id}/stream?token=<jwt>`
- `SseService` mantém um `ConcurrentHashMap<atendimentoId, List<SseEmitter>>`
- Quando uma mensagem é enviada, `notifySubscribers()` envia o evento `nova-mensagem` para todos os emitters conectados
- Timeout: **30 minutos** por conexão
- Emitters mortos são removidos automaticamente via `onCompletion`, `onTimeout`, `onError`

---

## Configuração Rápida

### Pré-requisitos
- Java 21+
- MySQL rodando em `localhost:3306`
- Navegador moderno

### Passos

```bash
# 1. Clone o repositório
git clone <url>

# 2. Configure o banco de dados
# Edite: server/src/main/resources/application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/cebe?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=

# 3. Execute o back-end (na pasta server/)
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows

# 4. Abra o front-end
# Abra client/src/pages/login.html no navegador
# Ou use uma extensão Live Server no VS Code
```

### Admin padrão (criado automaticamente)
| Campo | Valor |
|---|---|
| Email | `admin@cebe.com` |
| Senha | `admin123` |

> O admin só é criado se a tabela `admin` estiver vazia na primeira inicialização.

---

## Documentação (`/docs`)

| Arquivo | Descrição |
|---|---|
| `documentacao_tecnica.md` | Arquitetura, modelo de dados e segurança |
| `guia_estudo_completo.md` | Guia completo de estudo do projeto |
