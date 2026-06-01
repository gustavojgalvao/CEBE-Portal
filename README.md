# CEBE Portal — Monorepo Fullstack

Portal do Aluno do CEBE (Centro de Educação e Bem Estar), desenvolvido como monorepo com separação clara entre front-end e back-end.

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
├── public/           ← Arquivos estáticos (logo, imagens)
└── src/
    ├── assets/       ← Estilos CSS por página
    ├── components/   ← Componentes reutilizáveis (nav.js)
    ├── pages/        ← Páginas HTML (dashboard, login, matrícula…)
    └── services/     ← Integração com a API (notifications.js)
```

**Páginas disponíveis:**
- `login.html` — Autenticação do aluno
- `dashboard.html` — Painel principal
- `cursos.html` — Meus cursos e turmas
- `matricula.html` — Inscrição em novas turmas
- `atendimento.html` — Chamados de suporte
- `notificacoes.html` — Central de notificações
- `financeiro.html` — Situação financeira

---

## Back-end (`/server`)

API REST construída com **Java 21 + Spring Boot 3.5**, autenticação **JWT** e banco de dados **MySQL**.

```
server/
├── .mvn/wrapper/
├── src/
│   └── main/java/com/cebe/portal_aluno/
│       ├── config/       ← SecurityConfig, SecurityFilter (JWT)
│       ├── controller/   ← Endpoints REST (Aluno, Auth, Turma…)
│       ├── dto/          ← Request/Response DTOs (Records Java)
│       ├── entity/       ← Entidades JPA (Aluno, Turma, Matrícula…)
│       ├── exception/    ← GlobalExceptionHandler, exceções customizadas
│       ├── repository/   ← Interfaces Spring Data JPA
│       └── service/      ← Regras de negócio, TokenService
├── pom.xml
├── mvnw / mvnw.cmd
```

### Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 3.5 | Framework web |
| Spring Data JPA + Hibernate | Persistência ORM |
| Spring Security 6 + JWT (Auth0) | Autenticação stateless |
| Springdoc OpenAPI (Swagger) | Documentação da API |
| Lombok | Redução de boilerplate |
| MySQL | Banco de dados |

### Como executar o back-end

```bash
# Na pasta server/
./mvnw spring-boot:run
```

> O banco de dados deve estar disponível em `localhost:3306` com o schema `cebe`.  
> Configure as credenciais em `server/src/main/resources/application.properties`.

---

## Documentação (`/docs`)

| Arquivo | Descrição |
|---|---|
| `documentacao_tecnica.md` | Arquitetura, modelo de dados e segurança |
| `guia_estudo_completo.md` | Guia completo de estudo do projeto |
| `guia_integracao_frontend.md` | Como o front-end se comunica com a API |

---

## Configuração rápida

1. Clone o repositório
2. Configure `server/src/main/resources/application.properties` com suas credenciais MySQL
3. Execute o back-end: `cd server && ./mvnw spring-boot:run`
4. Abra as páginas do `client/src/pages/` no navegador
