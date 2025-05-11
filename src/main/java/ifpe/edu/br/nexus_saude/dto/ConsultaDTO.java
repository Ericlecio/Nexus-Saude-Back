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
	private Integer id;
    private Integer pacienteId;
    private Integer medicoId;
    private LocalDateTime data;
    private String especialidade;
    private String local;
    private Integer situacaoId; // ID da situação do agendamento

    // Construtor que converte de Consulta para ConsultaDTO
    public ConsultaDTO(Consulta consulta) {
        this.id = consulta.getConsultaId();
        this.pacienteId = consulta.getPaciente().getPacienteId(); // PacienteID agora é Integer
        this.medicoId = consulta.getMedico().getId(); // MedicoID agora é Integer
        this.data = consulta.getData(); // Se 'data' no modelo for LocalDateTime, converta para String
        this.especialidade = consulta.getEspecialidade();
        this.local = consulta.getLocal();
        this.situacaoId = consulta.getSituacao().getSituacaoId();
    }

    public Consulta toEntity(Paciente paciente, Medico medico, SituacaoAgendamento situacao) {
        return Consulta.builder()
                .consultaId(this.id)
                .paciente(paciente)
                .medico(medico)
                .data(this.data)
                .especialidade(this.especialidade)
                .local(this.local)
                .situacao(situacao)
                .createdAt(LocalDateTime.now()) // 🔹 Automatically set createdAt
                .updatedAt(LocalDateTime.now()) // 🔹 Set updatedAt too
                .build();
    }

}
