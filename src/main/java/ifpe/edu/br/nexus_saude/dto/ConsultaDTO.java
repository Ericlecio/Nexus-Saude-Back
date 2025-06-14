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
    private Integer situacaoId;

    public ConsultaDTO(Consulta consulta) {
        this.id = consulta.getConsultaId();
        this.pacienteId = consulta.getPaciente().getPacienteId();
        this.medicoId = consulta.getMedico().getId();
        this.data = consulta.getData();
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
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}