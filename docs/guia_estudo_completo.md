# Guia de Estudo Completo - Portal Aluno (Backend & Frontend)

Este documento é um guia de estudo aprofundado ("linha a linha") que explica a função de cada pasta, classe e arquivo do projeto **Portal Aluno**, cobrindo tanto a API (Backend) quanto a interface (Frontend). Ele serve para compreender a arquitetura e o funcionamento de um projeto moderno com Spring Boot 3.x, Spring Security 6.x, JPA, Banco de Dados, HTML5, CSS3 e JavaScript.

---

## Índice

**Backend (API Java/Spring)**
1. [Estrutura do Projeto Maven](#1-estrutura-do-projeto-maven)
2. [Entidades JPA (`entity`)](#2-entidades-jpa-entity)
3. [Repositórios (`repository`)](#3-repositórios-repository)
4. [Data Transfer Objects (`dto`)](#4-data-transfer-objects-dto)
5. [Segurança e Autenticação (`config` & `service`)](#5-segurança-e-autenticação-config--service)
6. [Serviços e Regras de Negócio (`service`)](#6-serviços-e-regras-de-negócio-service)
7. [Controladores REST (`controller`)](#7-controladores-rest-controller)
8. [Tratamento Global de Exceções (`exception`)](#8-tratamento-global-de-exceções-exception)

**Frontend (Client)**
9. [Estrutura de Pastas Frontend](#9-estrutura-de-pastas-frontend)
10. [O Sistema de Estilos (`assets/`)](#10-o-sistema-de-estilos-assets)
11. [Camada de Integração com API (`services/`)](#11-camada-de-integração-com-api-services)
12. [Injeção de Layout Compartilhado (`components/`)](#12-injeção-de-layout-compartilhado-components)
13. [As Páginas do Portal (`pages/`)](#13-as-páginas-do-portal-pages)
14. [Autenticação e Sessão (LocalStorage)](#14-autenticação-e-sessão-localstorage)

---

## 1. Estrutura do Projeto Maven

O projeto é estruturado utilizando o **Maven**, o gerenciador de dependências padrão do ecossistema Java.

* **`pom.xml` (Project Object Model)**: O "coração" do Maven. Nele são declaradas todas as bibliotecas (dependências) externas utilizadas (Spring Starter Web, Spring Security, JPA/Hibernate, Banco MySQL, Lombok, Swagger, JWT). Ele dita a versão do Java utilizada (Java 21) e controla o processo de compilação.
* **`src/main/resources`**:
  - **`application.properties`**: Arquivo de propriedades onde configuramos conexões do banco de dados (URL do MySQL, usuário, senha), o comportamento do Hibernate (`ddl-auto=update` para criar as tabelas automaticamente) e variáveis personalizadas (como a chave secreta do JWT).

---

## 2. Entidades JPA (`entity`)

As entidades representam a tabela do banco de dados MySQL expressas em forma de objetos Java.

### A. Mapeamentos Básicos
Toda entidade utiliza anotações do pacote `jakarta.persistence`:
* **`@Entity`**: Diz ao Spring/Hibernate que esta classe é uma tabela no banco.
* **`@Table(name = "nome_tabela")`**: Especifica o nome real da tabela física no MySQL.
* **`@Id`** e **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: Define que o campo é a chave primária física (ID) gerada de forma auto-incremental pelo banco.
* **`@Column(name = "CAMPO", nullable = false)`**: Mapeia as propriedades da coluna do banco (nome, tamanho físico, se aceita nulos).

### B. Anotações do Lombok (Simplificação de Código)
Para evitar escrever dezenas de getters, setters e construtores repetitivos:
* **`@Getter` / `@Setter`**: Gera automaticamente os métodos get/set para todas as variáveis em tempo de compilação.
* **`@NoArgsConstructor`**: Cria o construtor vazio padrão (exigido pelo JPA).
* **`@AllArgsConstructor`**: Cria um construtor que aceita todas as propriedades da classe.
* **`@Builder`**: Cria o padrão de projeto Builder, permitindo instanciar objetos de forma legível: `Aluno.builder().nome("Ana").build()`.

### C. Mapeamento de Relacionamentos
* **`@ManyToOne`** (Muitos para Um): Indica relacionamentos de chave estrangeira. Exemplo na classe `Turma`:
  ```java
  @ManyToOne
  @JoinColumn(name = "ID_CURSOS", nullable = false)
  private Cursos cursos;
  ```
  Isso diz que **Muitas** turmas pertencem a **Um** curso. O Hibernate cria a coluna `ID_CURSOS` física como chave estrangeira.

### D. Foco em `Aluno.java` (Interface `UserDetails`)
Para que o Spring Security gerencie a autenticação do Aluno, ele implementa a interface `UserDetails`:
* **`getAuthorities()`**: Retorna os papéis/permissões do usuário. Retornamos `List.of(new SimpleGrantedAuthority("ROLE_USER"))` (permissão padrão de usuário).
* **`getPassword()`**: Retorna a senha cadastrada (`this.senha`).
* **`getUsername()`**: Retorna o e-mail do usuário como identificador único (`this.email`).
* **`isAccountNonExpired()`, `isAccountNonLocked()`, `isCredentialsNonExpired()`, `isEnabled()`**: Retornam `true` indicando que a conta está ativa e válida.

---

## 3. Repositórios (`repository`)

Os repositórios são a camada que fala diretamente com o banco de dados.

* **`extends JpaRepository<Aluno, Integer>`**: Ao estender `JpaRepository`, o Spring injeta automaticamente métodos prontos de banco de dados (`save`, `findAll`, `findById`, `delete`), eliminando a necessidade de escrever SQL manualmente.
* **Query Methods (Exemplo: `findByEmail`)**:
  ```java
  Optional<Aluno> findByEmail(String email);
  ```
  O Spring Data JPA é inteligente: ele analisa o nome do método (`findBy` + `Email`) e cria em tempo de execução a query SQL correspondente: `SELECT * FROM aluno WHERE email = ?`. Retorna um `Optional` para tratar casos onde o e-mail não existe na base.

---

## 4. Data Transfer Objects (`dto`)

Os **DTOs** são classes que carregam dados entre as requisições HTTP e a API.

* **Por que usar DTOs?**
  - **Segurança**: Evita expor sua entidade de banco de dados diretamente na internet (ex: ocultar a senha na listagem pública de alunos).
  - **Flexibilidade**: Permite receber dados em formatos diferentes dos salvos no banco.
* **Java `record`**: Substituímos as classes antigas por `record`. O record é uma classe especial do Java moderno (Java 16+) projetada exclusivamente para transportar dados. Ele é **imutável** (não pode ser alterado após criado) e gera automaticamente getters simplificados (ex: `dto.nome()` em vez de `dto.getNome()`), construtores, `toString` e `equals` em apenas uma linha.
  ```java
  public record AlunoRequestDTO(String nome, String email, String senha) {}
  ```
* **Request DTO**: Payload enviado pelo cliente (ex: Dados de Cadastro ou Credenciais de Login).
* **Response DTO**: Payload devolvido pela API para o cliente.

---

## 5. Segurança e Autenticação (`config` & `service`)

Esta camada protege a aplicação, gerencia criptografia e valida tokens de acesso.

### A. `SecurityConfig.java` (O Porteiro da API)
Esta classe configura as regras gerais de segurança da web:
* **`@Configuration` & `@EnableWebSecurity`**: Avisa ao Spring que esta classe define regras globais de segurança.
* **`securityFilterChain`**: Configura o comportamento das requisições HTTP:
  - **`.csrf(csrf -> csrf.disable())`**: Desabilita o CSRF, que é dispensável para APIs REST que não armazenam sessões em Cookies.
  - **`.sessionManagement(...)`**: Define que a sessão é **STATELESS** (sem estado). O servidor não lembra de sessões ou cookies; cada requisição deve conter o token JWT para ser autenticada de forma independente.
  - **`.authorizeHttpRequests(...)`**: Controla o acesso de cada rota. Permite livre acesso a algumas rotas públicas (como cadastro de novos alunos e login), enquanto bloqueia o restante.
  - **`.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)`**: Adiciona o nosso validador customizado de tokens JWT na fila de segurança antes do validador de senha padrão do Spring.
* **`PasswordEncoder` Bean**: Declara o `BCryptPasswordEncoder` que será injetado nos serviços para criptografar as senhas no banco.

### B. `TokenService.java` (Gerador de JWT)
* **JWT (JSON Web Token)**: Funciona como uma carteira de identidade digital assinada pelo servidor.
* **`generateToken(Aluno aluno)`**:
  - **HMAC256**: Assina o token com uma chave secreta privada para garantir que ele não seja forjado.
  - **`withSubject(aluno.getEmail())`**: Guarda o e-mail do aluno dentro do token de forma criptografada.
  - **`withExpiresAt`**: Configura a expiração do token para 2 horas após a sua emissão.
* **`validateToken(String token)`**: Lê um token recebido, verifica se a assinatura secreta confere e se ele não expirou. Retorna o e-mail (subject) do aluno se estiver válido.

### C. `SecurityFilter.java` (O Fiscal do Token)
Filtro que roda a cada requisição enviada ao servidor (`OncePerRequestFilter`):
1. **Recupera o Token**: Lê o cabeçalho HTTP da requisição e procura pela propriedade `Authorization` extraindo a chave após a palavra `Bearer ` ou diretamente da URL em conexões WebSockets/SSE.
2. **Valida**: Chama o `TokenService.validateToken()`.
3. **Autentica**: Se o e-mail retornado for válido, o filtro busca o usuário no banco de dados e injeta seus dados diretamente no contexto de segurança do Spring (`SecurityContextHolder`). O Spring passa a considerar a requisição como autenticada.

### D. `AuthorizationService.java` (Adaptador de Login)
Implementa `UserDetailsService`. Contém o método `loadUserByUsername(String username)` exigido pelo módulo interno do Spring Security para buscar o Usuário no banco pelo e-mail fornecido na tela de login.

---

## 6. Serviços e Regras de Negócio (`service`)

Os serviços concentram a inteligência e lógica de processamento do negócio (processos que não devem estar nas classes de controle ou de repositório).

### `AlunoService.java`
* **`@Service`**: Identifica a classe como um serviço Spring para que ela possa ser auto-injetada em outras classes através de injeção de dependência (`@Autowired`).
* **`criarAluno(...)` & `atualizar(...)`**:
  - Recebe os dados de requisição (`AlunoRequestDTO`).
  - **Criptografia**: Executa `passwordEncoder.encode(dto.senha())` para transformar a senha "123456" em um hash ininteligível (como `$2a$10$...`) antes de salvar no banco de dados.
  - Salva e chama `converterParaDTO` para devolver ao controlador a resposta formatada sem expor a senha criptografada.
* **Tratamento de ID Inválido**:
  ```java
  Aluno aluno = repository.findById(id)
          .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado..."));
  ```
  Se o ID consultado não existir, lança nossa exceção customizada `RecursoNaoEncontradoException`, que será tratada de forma elegante no interceptador global.

---

## 7. Controladores REST (`controller`)

Os controladores expõem as portas de entrada (endpoints) da API para a internet.

* **`@RestController`**: Combinação de `@Controller` com `@ResponseBody`. Diz ao Spring que todo retorno de método deve ser convertido automaticamente para formato JSON e entregue no corpo da resposta HTTP.
* **`@RequestMapping("/alunos")`**: Define a rota base unificada para as operações desse módulo (todos os métodos responderão sob `/alunos`).
* **`@CrossOrigin("*")`**: Permite o compartilhamento de recursos entre origens diferentes. Garante que interfaces frontend rodando em outras portas locais consigam acessar os endpoints sem erros de CORS.
* **ResponseEntity**: O objeto encapsulador do Spring que permite configurar o corpo de resposta JSON e o **Código de Status HTTP** (ex: `200 OK` para sucesso, `201 Created` para inserções, `204 No Content` para deleções bem-sucedidas).
* **Anotações de Requisição HTTP**:
  - **`@PostMapping`**: Inserção de dados.
  - **`@GetMapping`**: Busca ou listagem de dados.
  - **`@PutMapping`**: Atualização completa de dados.
  - **`@DeleteMapping`**: Exclusão de dados.
  - **`@PathVariable`**: Captura parâmetros variáveis enviados na URL (ex: `/alunos/{id}`).
  - **`@RequestBody`**: Mapeia o corpo da requisição JSON enviada no corpo HTTP direto para o parâmetro DTO.

---

## 8. Tratamento Global de Exceções (`exception`)

A camada de exceção garante que a API nunca quebre de forma brusca e sempre devolva uma resposta limpa e compreensível ao cliente se algo falhar.

* **`@RestControllerAdvice`**: Um interceptador que vigia todos os controladores REST do sistema. Se qualquer método do controlador lançar uma exceção de erro, o interceptador a captura e a trata antes de retornar ao usuário.
* **`@ExceptionHandler`**: Anotação colocada nos métodos para dizer qual tipo de erro específico eles tratam:
  - **`RecursoNaoEncontradoException.class`**: Captura erros de IDs inválidos e formata um JSON amigável com status HTTP **`404 Not Found`**.
  - **`MethodArgumentNotValidException.class`**: Captura erros de validação de dados em formulários. Mapeia cada propriedade inválida em uma lista contendo a propriedade e a mensagem de erro específica, devolvendo HTTP **`400 Bad Request`**.
  - **`Exception.class`**: O "filtro de segurança final". Qualquer erro imprevisto ou falha interna do Java é capturado aqui para evitar exibir o rastro de pilha de execução (Stack Trace) na internet, retornando um HTTP **`500 Internal Server Error`** seguro.
* **`ErrorResponseDTO`**: O registro record padrão utilizado para garantir que todo e qualquer erro do sistema siga rigorosamente a mesma assinatura de resposta.

---

## 9. Estrutura de Pastas Frontend

O frontend foi desenvolvido utilizando HTML5 semântico, JavaScript moderno (vanilla) e CSS3 puro. Ele é organizado da seguinte forma:

```text
client/
├── public/                 # Imagens e logos estáticos
└── src/
    ├── assets/             # Arquivos de estilização (CSS)
    ├── components/         # Scripts para injetar componentes globais (ex: menu)
    ├── pages/              # Páginas HTML visuais da aplicação
    └── services/           # Integrações de JavaScript com a API (fetch)
```

---

## 10. O Sistema de Estilos (`assets/`)

O design do portal foi construído do zero utilizando variáveis CSS modernas (design tokens) e layouts responsivos (Flexbox e CSS Grid).

* **`style.css`**: 
  * Define as **variáveis globais** (`:root`) como a paleta de cores institucional (baseada no verde `#004f2b`), espaçamentos padronizados e tipografia (`Lexend`).
  * Contém a folha de estilos base para o formulário de login e o fluxo de matrícula passo a passo.
* **`dashboard.css`**: Estiliza o layout geral da área interna do aluno, incluindo a barra lateral (Sidebar), a barra superior (Topbar) e o layout responsivo de grade (Grid).
* **Estilos de Módulo**: Cada tela interna possui seu próprio arquivo CSS para melhor organização e performance:
  * `atendimento.css`: Layout de chat em tempo real e listagem de chamados.
  * `cursos.css`: Layout dos cards de cursos, progresso e detalhes das aulas.
  * `financeiro.css`: Tabela de pagamentos, badges de status (`Pago`, `Pendente`) e pop-up de 2ª via.
  * `notificacoes.css`: Linha do tempo das notificações do sistema.

---

## 11. Camada de Integração com API (`services/`)

Toda a comunicação com o back-end Java/Spring Boot está centralizada nessa pasta. Os arquivos enviam dados em formato JSON usando as APIs padrão do navegador.

> **Importante:** Como os scripts são carregados globalmente no HTML, o **`api.js` deve ser importado antes de qualquer outro serviço** para que a função `apiFetch` esteja disponível.

* **`api.js`**:
  * Define a URL base da API (`http://localhost:8080`).
  * Expõe a função `apiFetch(rota, opcoes)` que adiciona automaticamente o tipo de conteúdo JSON e anexa o token de autenticação JWT (`Authorization: Bearer <token>`) caso o usuário esteja logado.
* **`auth.js`**: Controla o login do usuário usando CPF e data de nascimento, além de gerenciar o cadastro de novos alunos.
* **`cursos.js`**: Obtém a listagem de cursos e turmas em que o aluno pode se matricular.
* **`matriculas.js`**: Realiza o envio da inscrição em cursos.
* **`atendimento.js`**: Faz a ponte para abrir chamados, ler mensagens do suporte e atualizar ou excluir chamados.
* **`notifications.js`**: Gerencia o fluxo de avisos, armazenando no localStorage e notificando o menu em tempo real de novas mensagens não lidas.

---

## 12. Injeção de Layout Compartilhado (`components/`)

Para evitar duplicar o menu e o topo em todas as páginas internas, o projeto usa um injetor dinâmico de HTML:

* **`nav.js`**:
  * Executado automaticamente ao carregar a página.
  * Injeta a **Sidebar** (menu esquerdo em desktops), a **Topbar** (perfil, logout, indicador de avisos) e o **Bottom Navigation** (menu inferior estilo app mobile em telas menores).
  * Atualiza automaticamente o indicador numérico (badge) vermelho de notificações não lidas e ativa o destaque visual da página atual na navegação.

---

## 13. As Páginas do Portal (`pages/`)

* **`login.html`**: A porta de entrada do portal. Requer o CPF e a data de nascimento.
* **`matricula.html`**: Formulário interativo dividido em 3 etapas (Dados Pessoais → Seleção de Curso → Criação de Acesso) para novos alunos se inscreverem.
* **`dashboard.html`**: Tela principal de boas-vindas do aluno, exibindo avisos rápidos de próximas aulas e progresso geral.
* **`cursos.html`**: Painel exibindo a lista de cursos ativos, professores responsáveis e detalhes das aulas.
* **`atendimento.html`**: Tela de suporte ao estilo "chat" onde o aluno pode conversar com a equipe administrativa via SSE em tempo real.
* **`financeiro.html`**: Central de boletos e histórico de pagamentos, com opção de download de 2ª via.
* **`notificacoes.html`**: Central de avisos e notificações gerais.

---

## 14. Autenticação e Sessão (LocalStorage)

O frontend usa o `localStorage` do navegador para manter a sessão do usuário ativa e sincronizada:

1. **`cebe_token`**: Guarda o token criptografado (JWT) retornado pelo back-end após um login bem-sucedido. É deletado quando o usuário clica em "Sair" (Logout).
2. **`cebe_role`**: Guarda o papel do usuário (ex: `ALUNO`, `ADMIN`) para redirecionamento correto e segurança no cliente.
3. **`cebe_notificacoes`**: Armazena as notificações locais da aplicação.
4. **`cebe_notif_sync`**: Usado pelo `nav.js` para sincronizar o contador de mensagens não lidas caso o aluno abra o portal em várias abas do navegador ao mesmo tempo.
