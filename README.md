# CEBE Portal

Projeto web completo (Fullstack) seguindo o padrão de separação entre cliente e servidor.

## Estrutura do Projeto

### 1. Raiz do Projeto (`/`)
- `client/`: Contém todo o código da interface (Front-end).
- `server/`: Contém toda a lógica de API e Banco de Dados (Back-end).
- `docs/`: Para armazenamento de wireframes, requisitos e modelagem de dados da Entrega 1.

### 2. Front-end (`/client`)
- `public/`: Arquivos estáticos como a logo do CEBE e imagens institucionais.
- `src/components/`: Componentes reutilizáveis (botões de chamado, cards de cursos, inputs de login).
- `src/pages/`: Páginas principais (Login, Dashboard, Matrícula, Atendimento, Notificações).
- `src/assets/`: Estilos CSS (Tailwind), fontes e ícones.
- `src/services/`: Configuração de integração com a API (Axios/Fetch).

### 3. Back-end (`/server`)
- `src/models/`: Esquemas do banco de dados (Aluno, Curso, Matrícula, Mensagem).
- `src/controllers/`: Lógica das rotas (Processamento de login e criação de matrículas).
- `src/routes/`: Definição dos endpoints da API REST.
- `src/database/`: Conexão com o banco de dados (MySQL).
- `src/middlewares/`: Validações de segurança e autenticação por CPF.
