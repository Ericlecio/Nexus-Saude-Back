package ifpe.edu.br.nexus_saude.dto;
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
        this.id = consulta.getId();
        this.pacienteId = consulta.getPaciente().getId();
        this.medicoId = consulta.getMedico().getId();
        this.data = consulta.getData();
        this.especialidade = consulta.getEspecialidade();
        this.local = consulta.getLocal();
        this.situacaoId = consulta.getSituacao().getSituacaoId();
    }

    public Consulta toEntity(Paciente paciente, Medico medico, SituacaoAgendamento situacao) {
        Consulta consulta = new Consulta();
        consulta.setId(this.id);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setData(this.data);
        consulta.setEspecialidade(this.especialidade);
        consulta.setLocal(this.local);
        consulta.setSituacao(situacao);
        return consulta;
    }
}
