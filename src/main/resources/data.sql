-- Situações padrão
INSERT INTO situacao_agendamento (descricao) VALUES ('Agendado');
INSERT INTO situacao_agendamento (descricao) VALUES ('Cancelado');
INSERT INTO situacao_agendamento (descricao) VALUES ('Finalizado');

-- Pacientes padrão
INSERT INTO paciente (
    cpf,
    data_cadastro,
    data_nascimento,
    email,
    nome_completo,
    plano_saude,
    senha,
    telefone,
    created_at,
    updated_at
) VALUES
('12345678901', NOW(), '1980-01-01', 'paciente1@example.com', 'Paciente Um', 'Plano A', 'senha123', '(81)99999-0001', NOW(), NOW()),
('12345678902', NOW(), '1990-02-02', 'paciente2@example.com', 'Paciente Dois', 'Plano B', 'senha123', '(81)99999-0002', NOW(), NOW()),
('12345678903', NOW(), '2000-03-03', 'paciente3@example.com', 'Paciente Tres', 'Plano C', 'senha123', '(81)99999-0003', NOW(), NOW());

-- Médicos padrão
INSERT INTO medico (
    cpf,
    crm,
    data_cadastro,
    data_nascimento,
    email,
    especialidade,
    nome,
    senha,
    sexo,
    telefone_consultorio,
    tempo_consulta,
    uf,
    valor_consulta,
    created_at,
    updated_at
) VALUES
('98765432101', 'CRM10001', NOW(), '1975-05-05', 'medico1@example.com', 'Cardiologia', 'Dr. Cardiologista', 'senha123', 'M', '(81)98888-0001', 30, 'PE', 250.00, NOW(), NOW()),
('98765432102', 'CRM10002', NOW(), '1980-06-10', 'medico2@example.com', 'Dermatologia', 'Dra. Dermatologista', 'senha123', 'F', '(81)98888-0002', 45, 'PE', 300.00, NOW(), NOW()),
('98765432103', 'CRM10003', NOW(), '1970-07-15', 'medico3@example.com', 'Pediatria', 'Dr. Pediatra', 'senha123', 'M', '(81)98888-0003', 20, 'PE', 180.00, NOW(), NOW());

-- Dias de Atendimento para cada médico (ajuste medico_id conforme auto-increment)

-- Médico 1 (id=1): Segunda 08:00-12:00, Quarta 13:00-17:00
INSERT INTO dias_atendimento (medico_id, dia_semana, horario, created_at, updated_at) VALUES
(1, 'Segunda', '08:00', NOW(), NOW()),
(1, 'Segunda', '12:00', NOW(), NOW()),
(1, 'Quarta', '13:00', NOW(), NOW()),
(1, 'Quarta', '17:00', NOW(), NOW());

-- Médico 2 (id=2): Terça 09:00-13:00, Quinta 14:00-18:00
INSERT INTO dias_atendimento (medico_id, dia_semana, horario, created_at, updated_at) VALUES
(2, 'Terça', '09:00', NOW(), NOW()),
(2, 'Terça', '13:00', NOW(), NOW()),
(2, 'Quinta', '14:00', NOW(), NOW()),
(2, 'Quinta', '18:00', NOW(), NOW());

-- Médico 3 (id=3): Segunda 07:30-11:30, Sexta 08:00-12:00
INSERT INTO dias_atendimento (medico_id, dia_semana, horario, created_at, updated_at) VALUES
(3, 'Segunda', '07:30', NOW(), NOW()),
(3, 'Segunda', '11:30', NOW(), NOW()),
(3, 'Sexta', '08:00', NOW(), NOW()),
(3, 'Sexta', '12:00', NOW(), NOW());
