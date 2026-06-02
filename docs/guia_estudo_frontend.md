# Guia de Estudo Completo - Frontend Portal Aluno

Este documento é um guia detalhado da arquitetura e funcionamento do **Frontend** do projeto **CEBE Portal**. Ele explica a função de cada diretório, arquivo, o sistema de estilos e como as chamadas para o back-end (API) são feitas.

---

## Índice
1. [Estrutura de Pastas](#1-estrutura-de-pastas)
2. [O Sistema de Estilos (`assets/`)](#2-o-sistema-de-estilos-assets)
3. [Camada de Integração com API (`services/`)](#3-camada-de-integração-com-api-services)
4. [Injeção de Layout Compartilhado (`components/`)](#4-injeção-de-layout-compartilhado-components)
5. [As Páginas do Portal (`pages/`)](#5-as-páginas-do-portal-pages)
6. [Autenticação e Sessão (LocalStorage)](#6-autenticação-e-sessão-localstorage)

---

## 1. Estrutura de Pastas

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

## 2. O Sistema de Estilos (`assets/`)

O design do portal foi construído do zero utilizando variáveis CSS modernas (design tokens) e layouts responsivos (Flexbox e CSS Grid).

* **[style.css](file:///e:/Projetos/CEBE-Portal/client/src/assets/style.css)**: 
  * Define as **variáveis globais** (`:root`) como a paleta de cores institucional (baseada no verde `#004f2b`), espaçamentos padronizados e tipografia (`Lexend`).
  * Contém a folha de estilos base para o formulário de login e o fluxo de matrícula passo a passo.
* **[dashboard.css](file:///e:/Projetos/CEBE-Portal/client/src/assets/dashboard.css)**: Estiliza o layout geral da área interna do aluno, incluindo a barra lateral (Sidebar), a barra superior (Topbar) e o layout responsivo de grade (Grid).
* **Estilos de Módulo**: Cada tela interna possui seu próprio arquivo CSS para melhor organização e performance:
  * `atendimento.css`: Layout de chat em tempo real e listagem de chamados.
  * `cursos.css`: Layout dos cards de cursos, progresso e detalhes das aulas.
  * `financeiro.css`: Tabela de pagamentos, badges de status (`Pago`, `Pendente`) e pop-up de 2ª via.
  * `notificacoes.css`: Linha do tempo das notificações do sistema.

---

## 3. Camada de Integração com API (`services/`)

Toda a comunicação com o back-end Java/Spring Boot está centralizada nessa pasta. Os arquivos enviam dados em formato JSON usando as APIs padrão do navegador.

> [!IMPORTANT]
> Como os scripts são carregados globalmente no HTML, o **`api.js` deve ser importado antes de qualquer outro serviço** para que a função `apiFetch` esteja disponível.

* **[api.js](file:///e:/Projetos/CEBE-Portal/client/src/services/api.js)**:
  * Define a URL base da API (`http://localhost:8080`).
  * Expõe a função `apiFetch(rota, opcoes)` que adiciona automaticamente o tipo de conteúdo JSON e anexa o token de autenticação JWT (`Authorization: Bearer <token>`) caso o aluno esteja logado.
* **[auth.js](file:///e:/Projetos/CEBE-Portal/client/src/services/auth.js)**: Controla o login do usuário usando CPF e data de nascimento, além de gerenciar o cadastro de novos alunos.
* **[cursos.js](file:///e:/Projetos/CEBE-Portal/client/src/services/cursos.js)**: Obtém a listagem de cursos e turmas em que o aluno pode se matricular.
* **[matriculas.js](file:///e:/Projetos/CEBE-Portal/client/src/services/matriculas.js)**: Realiza o envio da inscrição em cursos.
* **[atendimento.js](file:///e:/Projetos/CEBE-Portal/client/src/services/atendimento.js)**: Faz a ponte para abrir chamados, ler mensagens do suporte e atualizar ou excluir chamados.
* **[notifications.js](file:///e:/Projetos/CEBE-Portal/client/src/services/notifications.js)**: Gerencia o fluxo de avisos, armazenando no localStorage e notificando o menu em tempo real de novas mensagens não lidas.

---

## 4. Injeção de Layout Compartilhado (`components/`)

Para evitar duplicar o menu e o topo em todas as páginas internas, o projeto usa um injetor dinâmico de HTML:

* **[nav.js](file:///e:/Projetos/CEBE-Portal/client/src/components/nav.js)**:
  * Executado automaticamente ao carregar a página.
  * Injeta a **Sidebar** (menu esquerdo em desktops), a **Topbar** (perfil, logout, indicador de avisos) e o **Bottom Navigation** (menu inferior estilo app mobile em telas menores).
  * Atualiza automaticamente o indicador numérico (badge) vermelho de notificações não lidas e ativa o destaque visual da página atual na navegação.

---

## 5. As Páginas do Portal (`pages/`)

* **`login.html`**: A porta de entrada do portal. Requer o CPF e a data de nascimento.
* **`matricula.html`**: Formulário interativo dividido em 3 etapas (Dados Pessoais → Seleção de Curso → Criação de Acesso) para novos alunos se inscreverem.
* **`dashboard.html`**: Tela principal de boas-vindas do aluno, exibindo avisos rápidos de próximas aulas e progresso geral.
* **`cursos.html`**: Painel exibindo a lista de cursos ativos, professores responsáveis e detalhes das aulas.
* **`atendimento.html`**: Tela de suporte ao estilo "chat" onde o aluno pode conversar com a equipe administrativa.
* **`financeiro.html`**: Central de boletos e histórico de pagamentos, com opção de download de 2ª via.
* **`notificacoes.html`**: Central de avisos e notificações gerais.

---

## 6. Autenticação e Sessão (LocalStorage)

O frontend usa o `localStorage` do navegador para manter a sessão do usuário ativa e sincronizada:

1. **`cebe_token`**: Guarda o token criptografado (JWT) retornado pelo back-end após um login bem-sucedido. É deletado quando o usuário clica em "Sair" (Logout).
2. **`cebe_notificacoes`**: Armazena as notificações locais da aplicação.
3. **`cebe_notif_sync`**: Usado pelo `nav.js` para sincronizar o contador de mensagens não lidas caso o aluno abra o portal em várias abas do navegador ao mesmo tempo.
