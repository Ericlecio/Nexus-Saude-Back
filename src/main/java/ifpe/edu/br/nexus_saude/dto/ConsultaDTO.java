package ifpe.edu.br.nexus_saude.dto;

import java.time.LocalDateTime;

import ifpe.edu.br.nexus_saude.model.Consulta;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaDTO {
    private Integer id; // ID da consulta
    private Integer pacienteId; // Alterado de String para Integer
    private Integer medicoId; // ID do médico
    private String data; // Data como String, mas pode ser LocalDateTime se necessário
    private String especialidade;
    private String local;
    private Integer situacaoId; // ID da situação do agendamento

    // Construtor que converte de Consulta para ConsultaDTO
    public ConsultaDTO(Consulta consulta) {
        this.id = consulta.getConsultaId();
        this.pacienteId = consulta.getPaciente().getPacienteId(); // PacienteID agora é Integer
        this.medicoId = consulta.getMedico().getId(); // MedicoID agora é Integer
        this.data = consulta.getData().toString(); // Se 'data' no modelo for LocalDateTime, converta para String
        this.especialidade = consulta.getEspecialidade();
        this.local = consulta.getLocal();
        this.situacaoId = consulta.getSituacao().getSituacaoId();
    }

    public Consulta toEntity(Paciente paciente, Medico medico, SituacaoAgendamento situacao) {
        Consulta consulta = new Consulta();
        consulta.setConsultaId(id);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(LocalDateTime.parse(this.data)); // Converte 'data' de String para LocalDateTime
        consulta.setEspecialidade(this.especialidade);
        consulta.setLocal(this.local);
        consulta.setSituacao(situacao);
        return consulta;
    }

}
