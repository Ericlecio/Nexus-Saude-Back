-- Situações padrão
INSERT INTO situacao_agendamento (descricao) VALUES ('Agendado');
INSERT INTO situacao_agendamento (descricao) VALUES ('Cancelado');
INSERT INTO situacao_agendamento (descricao) VALUES ('Finalizado');

-- Paciente padrão (ajuste os campos conforme sua tabela)
INSERT INTO paciente (cpf, data_cadastro, data_nascimento, email, nome_completo, plano_saude, senha, telefone, created_at, updated_at)
VALUES ('12345678901', NOW(), '1980-01-01', 'paciente1@example.com', 'Paciente Teste', 'Plano A', 'senha123', '(81)99999-9999', NOW(), NOW());

-- Médico padrão (ajuste os campos conforme sua tabela)
INSERT INTO medico (cpf, crm, data_cadastro, data_nascimento, email, especialidade, nome, senha, sexo, telefone_consultorio, tempo_consulta, uf, valor_consulta, created_at, updated_at)
VALUES ('98765432100', 'CRM12345', NOW(), '1975-05-05', 'medico1@example.com', 'Cardiologia', 'Dr. Teste', 'senha123', 'M', '(81)98888-8888', 30, 'PE', 200.00, NOW(), NOW());
