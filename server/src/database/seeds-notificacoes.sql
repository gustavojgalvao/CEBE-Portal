-- ============================================================
-- SEEDS: Adiciona notificações de teste para todos os alunos
-- Execute no MySQL Workbench
-- ============================================================

USE cebe;

-- Notificação de Boas vindas (não lida)
INSERT INTO notificacao (id_aluno, mensagem, lida, data_hora, tipo)
SELECT id, 'Sua matrícula foi confirmada com sucesso! Bem-vindo(a) ao CEBE.', 0, NOW(), 'success' FROM aluno;

-- Notificação de Documentação (não lida)
INSERT INTO notificacao (id_aluno, mensagem, lida, data_hora, tipo)
SELECT id, 'Lembrete: A documentação pendente deve ser entregue na secretaria.', 0, DATE_SUB(NOW(), INTERVAL 2 HOUR), 'warning' FROM aluno;

-- Notificação de Boleto (lida)
INSERT INTO notificacao (id_aluno, mensagem, lida, data_hora, tipo)
SELECT id, 'O boleto da sua mensalidade de maio já está disponível no financeiro.', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), 'info' FROM aluno;

-- Confirmação
SELECT * FROM notificacao;
