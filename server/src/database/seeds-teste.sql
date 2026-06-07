-- ============================================================
-- SEEDS DE TESTE — CEBE Portal
-- Execute DEPOIS de rodar o banco-CEBE.sql
-- ============================================================

USE cebe;

-- -------------------------------------------------------------
-- 1. Corrige o ENUM do atendimento para bater com o Java
--    (banco tinha 'Em andamento', Java usa 'Em_andamento')
-- -------------------------------------------------------------
ALTER TABLE atendimento
  MODIFY COLUMN STATUS_ATENDIMENTO
  ENUM('Finalizado','Pendente','Em_andamento') NOT NULL;

-- -------------------------------------------------------------
-- 2. Professores de exemplo
-- -------------------------------------------------------------
INSERT INTO professor (NOME, EMAIL, ESPECIALIZACAO) VALUES
  ('Mariana Silva',  'mariana@cebe.edu.br',  'Confeitaria'),
  ('Carlos Mendes',  'carlos@cebe.edu.br',   'Auxiliar de Escritório'),
  ('Patricia Souza', 'patricia@cebe.edu.br', 'Informática Básica');

-- -------------------------------------------------------------
-- 3. Cursos de exemplo
-- -------------------------------------------------------------
INSERT INTO cursos (NOME, CARGA_HORARIA) VALUES
  ('Confeitaria',    160),
  ('Auxiliar Escri', 120),
  ('Informatica',    80);

-- -------------------------------------------------------------
-- 4. Turmas de exemplo
--    (vinculadas a curso + professor)
-- -------------------------------------------------------------
INSERT INTO turma (ID_CURSOS, ID_PROFESSOR, TURNO, LOTACAO_MAXIMA, VAGAS_OCUPADAS) VALUES
  (1, 1, 'Matutino',    20, 0),   -- Confeitaria - Turma Manhã
  (1, 1, 'Vespertino',  20, 0),   -- Confeitaria - Turma Tarde
  (2, 2, 'Matutino',    15, 0),   -- Auxiliar Escritório - Manhã
  (3, 3, 'Vespertino',  25, 0);   -- Informática - Tarde

-- -------------------------------------------------------------
-- Pronto! Agora você pode:
--   1. Acessar matricula.html e criar um aluno
--   2. Fazer login com o CPF e data de nascimento cadastrados
--   3. Testar o dashboard, atendimento etc.
-- -------------------------------------------------------------
