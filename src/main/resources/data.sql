-- Situações padrão
INSERT IGNORE  INTO situacao_agendamento (descricao) VALUES ('Agendado');
INSERT IGNORE  INTO situacao_agendamento (descricao) VALUES ('Cancelado');
INSERT IGNORE  INTO situacao_agendamento (descricao) VALUES ('Finalizado');

-- Papeis (Roles)
INSERT IGNORE INTO papeis (id, nome) VALUES (1, 'PACIENTE'), (2, 'MEDICO'), (3, 'ADMIN');

-- Usuário ADMIN padrão (ID: 11)
INSERT IGNORE  INTO usuarios (id, email, senha, ativo) VALUES
(11, 'admin@nexus.com', '$2a$10$0ITvqbgsj1d4U6eKHdSM6utNeohSwFjPx65J0t/TAHNKBlqZzSD3y', true);
 
-- Associar papel ADMIN ao usuário admin (papel_id = 3)
INSERT IGNORE  INTO usuarios_papeis (usuario_id, papel_id) VALUES (11, 3);

-- Inserir admin na tabela correta (admin)
INSERT IGNORE  INTO admin (usuario_id) VALUES (11);

-- Usuários para os Pacientes
INSERT IGNORE  INTO usuarios (id, email, senha, ativo) VALUES
(1, 'maria.silva@example.com', '$2a$10$HASHFICTICIO1', true),
(2, 'joao.souza@example.com', '$2a$10$HASHFICTICIO1', true),
(3, 'ana.costa@example.com', '$2a$10$HASHFICTICIO1', true),
(4, 'bruno.oliveira@example.com', '$2a$10$HASHFICTICIO1', true),
(5, 'luciana.martins@example.com', '$2a$10$HASHFICTICIO1', true);

-- Associar Papel de PACIENTE aos usuários
INSERT IGNORE  INTO usuarios_papeis (usuario_id, papel_id) VALUES (1, 1), (2, 1), (3, 1), (4, 1), (5, 1);

-- Pacientes
INSERT IGNORE  INTO paciente (
  usuario_id, nome_completo, telefone, cpf, data_nascimento,
  plano_saude, data_cadastro, created_at, updated_at
) VALUES
(1, 'Maria Silva', '(81)98765-4321', '123.456.789-00', '1985-03-15', 'Unimed', NOW(), NOW(), NOW()),
(2, 'João Souza', '(81)99876-5432', '234.567.890-11', '1992-08-20', 'Hapvida', NOW(), NOW(), NOW()),
(3, 'Ana Costa', '(81)99654-3210', '345.678.901-22', '2001-12-05', 'Bradesco Saúde', NOW(), NOW(), NOW()),
(4, 'Bruno Oliveira', '(81)99555-1234', '456.789.012-33', '1988-04-10', 'Amil', NOW(), NOW(), NOW()),
(5, 'Luciana Martins', '(81)99444-5678', '567.890.123-44', '1995-07-25', 'SulAmérica', NOW(), NOW(), NOW());

-- Usuários para os Médicos
INSERT IGNORE  INTO usuarios (id, email, senha, ativo) VALUES
(6, 'roberto.lima@example.com', '$2a$10$HASHFICTICIO1', true),
(7, 'fernanda.alves@example.com', '$2a$10$HASHFICTICIO1', true),
(8, 'paulo.medeiros@example.com', '$2a$10$HASHFICTICIO1', true),
(9, 'juliana.ramos@example.com', '$2a$10$HASHFICTICIO1', true),
(10, 'marcelo.teixeira@example.com', '$2a$10$HASHFICTICIO1', true);

-- Associar Papel de MEDICO aos usuários
INSERT IGNORE  INTO usuarios_papeis (usuario_id, papel_id) VALUES (6, 2), (7, 2), (8, 2), (9, 2), (10, 2);

-- Médicos
INSERT IGNORE  INTO medico (
  usuario_id, nome, crm, especialidade, cpf, sexo, telefone_consultorio,
  tempo_consulta, uf, valor_consulta, data_nascimento, data_cadastro, created_at, updated_at
) VALUES
(6, 'Roberto Lima', 'CRM-12345', 'Cardiologia', '678.901.234-55', 'M', '(81)98700-1111', 30, 'PE', 300.00, '1970-07-10', NOW(), NOW(), NOW()),
(7, 'Fernanda Alves', 'CRM-23456', 'Dermatologia', '789.012.345-66', 'F', '(81)98700-2222', 40, 'PE', 350.00, '1982-05-22', NOW(), NOW(), NOW()),
(8, 'Paulo Medeiros', 'CRM-34567', 'Pediatria', '890.123.456-77', 'M', '(81)98700-3333', 20, 'PE', 250.00, '1975-11-30', NOW(), NOW(), NOW()),
(9, 'Juliana Ramos', 'CRM-45678', 'Ginecologia', '901.234.567-88', 'F', '(81)98700-4444', 30, 'PE', 270.00, '1983-03-18', NOW(), NOW(), NOW()),
(10, 'Marcelo Teixeira', 'CRM-56789', 'Ortopedia', '012.345.678-99', 'M', '(81)98700-5555', 45, 'PE', 320.00, '1979-09-09', NOW(), NOW(), NOW());

-- Dias de Atendimento
INSERT IGNORE  INTO dias_atendimento (medico_id, dia_semana, horario, created_at, updated_at) VALUES
(1, 'Segunda', '08:00', NOW(), NOW()), (1, 'Segunda', '12:00', NOW(), NOW()),
(1, 'Quarta', '13:00', NOW(), NOW()), (1, 'Quarta', '17:00', NOW(), NOW()),
(2, 'Terça', '09:00', NOW(), NOW()), (2, 'Terça', '13:00', NOW(), NOW()),
(2, 'Quinta', '14:00', NOW(), NOW()), (2, 'Quinta', '18:00', NOW(), NOW()),
(3, 'Segunda', '07:30', NOW(), NOW()), (3, 'Segunda', '11:30', NOW(), NOW()),
(3, 'Sexta', '08:00', NOW(), NOW()), (3, 'Sexta', '12:00', NOW(), NOW()),
(4, 'Quarta', '10:00', NOW(), NOW()), (4, 'Quarta', '14:00', NOW(), NOW()),
(4, 'Sexta', '09:30', NOW(), NOW()), (4, 'Sexta', '13:30', NOW(), NOW()),
(5, 'Terça', '10:30', NOW(), NOW()), (5, 'Terça', '15:00', NOW(), NOW()),
(5, 'Quinta', '08:30', NOW(), NOW()), (5, 'Quinta', '11:30', NOW(), NOW());
