# Guia de Integração Frontend - API Portal Aluno

Este guia foi elaborado para desenvolvedores frontend (e qualquer pessoa que não possua conhecimento profundo de Java/Spring Boot) entenderem como consumir a API **Portal Aluno** e integrá-la a uma interface visual (React, Vue, Angular, HTML/JS puro, etc.).

---

## 1. Como a API Funciona?

A API é um conjunto de "portas de comunicação" (endpoints REST) que permitem ler, salvar, atualizar e deletar informações sobre 6 módulos principais:
1. **Alunos** (`/alunos`)
2. **Professores** (`/professores`)
3. **Cursos** (`/cursos`)
4. **Turmas** (`/turmas`)
5. **Matrículas** (`/matriculas`) (inscrição de alunos em turmas)
6. **Atendimentos** (`/atendimentos`) (histórico de chamados dos alunos)

Todos os dados trafegam no formato **JSON**, que é o padrão universal para JavaScript.

---

## 2. Configurações Prontas para o Frontend

Para facilitar o seu trabalho de desenvolvimento local, a API já vem com duas facilidades prontas:

* **CORS Liberado**: A anotação `@CrossOrigin("*")` está ativada. Isso significa que você pode fazer requisições à API diretamente do seu navegador (como `http://localhost:3000` ou `http://localhost:5173`) sem sofrer nenhum bloqueio de segurança.
* **Segurança Flexível (Modo Dev)**: A segurança com autenticação JWT está totalmente codificada, mas foi configurada no modo permissivo para desenvolvimento. **Você pode acessar qualquer endpoint sem precisar de token de acesso neste momento.** Quando quiser ativar o bloqueio de segurança, basta reverter a configuração no arquivo `SecurityConfig.java`.

---

## 3. Passo a Passo da Autenticação (JWT)

A autenticação é baseada em **JWT (JSON Web Tokens)**. Funciona assim:

1. **Cadastro**: O aluno se registra enviando seus dados e senha para `POST /alunos`. A senha é salva com criptografia forte no banco de dados.
2. **Login**: O aluno faz login enviando e-mail e senha para `POST /auth/login`.
3. **Token**: O servidor valida as credenciais e devolve um texto assinado digitalmente chamado **Token**.
4. **Armazenamento**: O seu código frontend deve salvar esse token no `localStorage` ou `sessionStorage` do navegador.
5. **Requisições Protegidas**: Em rotas que exigem segurança, você deve enviar esse token no cabeçalho (Header) de todas as requisições HTTP:
   `Authorization: Bearer <seu_token_aqui>`

---

## 4. Exemplos Práticos de Integração (JavaScript / Axios)

Abaixo estão os exemplos práticos de como consumir a API utilizando o JavaScript padrão do navegador.

### A. Cadastro de um Novo Aluno (`POST /alunos`)
```javascript
const cadastrarAluno = async (nome, telefone, cpf, email, senha) => {
  const response = await fetch("http://localhost:8080/alunos", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ nome, telefone, cpf, email, senha })
  });

  if (response.ok) {
    const dados = await response.json();
    console.log("Aluno cadastrado com sucesso:", dados);
  } else {
    const erro = await response.json();
    console.error("Falha no cadastro:", erro);
  }
};
```

### B. Login do Aluno e Captura do Token (`POST /auth/login`)
```javascript
const realizarLogin = async (email, senha) => {
  const response = await fetch("http://localhost:8080/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ email, senha })
  });

  if (response.ok) {
    const data = await response.json();
    // Salva o token retornado pela API no navegador
    localStorage.setItem("token_portal_aluno", data.token);
    console.log("Login bem-sucedido! Token armazenado.");
  } else {
    console.error("E-mail ou senha incorretos.");
  }
};
```

### C. Consumindo Endpoints Protegidos com o Token
Quando a segurança estiver ativada, envie o token no cabeçalho:
```javascript
const listarAlunos = async () => {
  const token = localStorage.getItem("token_portal_aluno");

  const response = await fetch("http://localhost:8080/alunos", {
    method: "GET",
    headers: {
      "Authorization": `Bearer ${token}` // Envia o token salvo
    }
  });

  if (response.ok) {
    const alunos = await response.json();
    console.log("Lista de alunos obtida:", alunos);
  } else {
    console.error("Acesso negado ou erro no servidor.");
  }
};
```

---

## 5. Como Ler os Erros da API?

A API possui um **Global Exception Handler** que padroniza todos os erros do sistema. Se uma requisição falhar (por exemplo, e-mail inválido, CPF duplicado, ou aluno inexistente), a API retornará uma resposta com status de erro (como `400` ou `404`) e o seguinte formato JSON:

```json
{
  "timestamp": "2026-05-29T15:12:08",
  "status": 400,
  "error": "Erro de Validação de Campos",
  "message": "Um ou mais campos contêm erros de validação",
  "path": "/alunos",
  "fields": [
    {
      "field": "email",
      "message": "deve ser um endereço de e-mail bem formado"
    }
  ]
}
```

No frontend, você pode capturar a lista `fields` e exibir mensagens de erro vermelhas diretamente sob cada campo correspondente do seu formulário HTML.

---

## 6. Testando de Forma Interativa (Swagger UI)

Antes de programar qualquer tela do Frontend, você pode testar todas as funcionalidades da API usando o console interativo do **Swagger**:

1. Inicie a API no Spring Boot.
2. Abra seu navegador em: `http://localhost:8080/swagger-ui/index.html`
3. Clique em qualquer rota (por exemplo, `POST /auth/login`).
4. Clique em **"Try it out"**, preencha os dados e clique em **"Execute"**.
5. O console exibirá a resposta exata devolvida pela API, ajudando a entender o formato esperado antes mesmo de escrever o código JS.
