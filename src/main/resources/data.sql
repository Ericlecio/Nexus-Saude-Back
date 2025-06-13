-- Situações padrão
INSERT INTO situacao_agendamento (descricao) VALUES ('Agendado');
INSERT INTO situacao_agendamento (descricao) VALUES ('Cancelado');
INSERT INTO situacao_agendamento (descricao) VALUES ('Finalizado');

-- Papeis (Roles)
INSERT INTO papeis (nome) VALUES ('PACIENTE'), ('MEDICO'), ('ADMIN');

-- Usuarios para os Pacientes
INSERT INTO usuarios (email, senha, ativo) VALUES
('paciente1@example.com', '$2a$10$3Z1J4B5v2a3N6E8C7D0F.eG/n5o8r.d8c.a6B5G4H3I2J1K0L.m', true),
('paciente2@example.com', '$2a$10$3Z1J4B5v2a3N6E8C7D0F.eG/n5o8r.d8c.a6B5G4H3I2J1K0L.m', true),
('paciente3@example.com', '$2a$10$3Z1J4B5v2a3N6E8C7D0F.eG/n5o8r.d8c.a6B5G4H3I2J1K0L.m', true);

-- Associar Papel de PACIENTE aos usuarios
-- (Assume user IDs 1, 2, 3 and role ID 1 for PACIENTE)
INSERT INTO usuarios_papeis (usuario_id, papel_id) VALUES (1, 1), (2, 1), (3, 1);

-- Pacientes (referenciando os usuarios)
INSERT INTO paciente (usuario_id, nome_completo, telefone, cpf, data_nascimento, plano_saude, data_cadastro, created_at, updated_at) VALUES
(1, 'Paciente Um', '(81)99999-0001', '12345678901', '1980-01-01', 'Plano A', NOW(), NOW(), NOW()),
(2, 'Paciente Dois', '(81)99999-0002', '12345678902', '1990-02-02', 'Plano B', NOW(), NOW(), NOW()),
(3, 'Paciente Tres', '(81)99999-0003', '12345678903', '2000-03-03', 'Plano C', NOW(), NOW(), NOW());


-- Usuarios para os Médicos
INSERT INTO usuarios (email, senha, ativo) VALUES
('medico1@example.com', '$2a$10$3Z1J4B5v2a3N6E8C7D0F.eG/n5o8r.d8c.a6B5G4H3I2J1K0L.m', true),
('medico2@example.com', '$2a$10$3Z1J4B5v2a3N6E8C7D0F.eG/n5o8r.d8c.a6B5G4H3I2J1K0L.m', true),
('medico3@example.com', '$2a$10$3Z1J4B5v2a3N6E8C7D0F.eG/n5o8r.d8c.a6B5G4H3I2J1K0L.m', true);

-- Associar Papel de MEDICO aos usuarios
-- (Assume user IDs 4, 5, 6 and role ID 2 for MEDICO)
INSERT INTO usuarios_papeis (usuario_id, papel_id) VALUES (4, 2), (5, 2), (6, 2);

-- Médicos (referenciando os usuarios)
INSERT INTO medico (usuario_id, nome, crm, especialidade, cpf, sexo, telefone_consultorio, tempo_consulta, uf, valor_consulta, data_nascimento, data_cadastro, created_at, updated_at) VALUES
(4, 'Dr. Cardiologista', 'CRM10001', 'Cardiologia', '98765432101', 'M', '(81)98888-0001', 30, 'PE', 250.00, '1975-05-05', NOW(), NOW(), NOW()),
(5, 'Dra. Dermatologista', 'CRM10002', 'Dermatologia', '98765432102', 'F', '(81)98888-0002', 45, 'PE', 300.00, '1980-06-10', NOW(), NOW(), NOW()),
(6, 'Dr. Pediatra', 'CRM10003', 'Pediatria', '98765432103', 'M', '(81)98888-0003', 20, 'PE', 180.00, '1970-07-15', NOW(), NOW(), NOW());


-- Dias de Atendimento para cada médico
-- (Assume medico IDs are 1, 2, 3)
INSERT INTO dias_atendimento (medico_id, dia_semana, horario, created_at, updated_at) VALUES
(1, 'Segunda', '08:00', NOW(), NOW()), (1, 'Segunda', '12:00', NOW(), NOW()),
(1, 'Quarta', '13:00', NOW(), NOW()), (1, 'Quarta', '17:00', NOW(), NOW()),
(2, 'Terça', '09:00', NOW(), NOW()), (2, 'Terça', '13:00', NOW(), NOW()),
(2, 'Quinta', '14:00', NOW(), NOW()), (2, 'Quinta', '18:00', NOW(), NOW()),
(3, 'Segunda', '07:30', NOW(), NOW()), (3, 'Segunda', '11:30', NOW(), NOW()),
(3, 'Sexta', '08:00', NOW(), NOW()), (3, 'Sexta', '12:00', NOW(), NOW());
