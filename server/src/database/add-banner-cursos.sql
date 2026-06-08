-- ============================================================
-- MIGRAÇÃO: Adiciona coluna BANNER_URL à tabela cursos
-- Execute no MySQL Workbench ou via linha de comando
-- ============================================================

USE cebe;

-- 1. Adiciona a coluna (caso ainda não exista)
ALTER TABLE cursos
  ADD COLUMN IF NOT EXISTS BANNER_URL VARCHAR(255) NULL,
  MODIFY COLUMN NOME VARCHAR(100) NOT NULL;

-- 2. Atualiza os banners dos cursos existentes (seeds-teste.sql)
UPDATE cursos SET BANNER_URL = '/client/public/banners/confeitaria.png'        WHERE NOME = 'Confeitaria';
UPDATE cursos SET BANNER_URL = '/client/public/banners/auxiliar-escritorio.png' WHERE NOME = 'Auxiliar Escri';
UPDATE cursos SET BANNER_URL = '/client/public/banners/informatica.png'         WHERE NOME = 'Informatica';

-- Confirmação
SELECT ID, NOME, CARGA_HORARIA, BANNER_URL FROM cursos;
