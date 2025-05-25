package ifpe.edu.br.nexus_saude.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import ifpe.edu.br.nexus_saude.model.Agendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDTO {
    private Integer agendamentoId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") // Para desserialização correta do JSON
    private LocalDateTime data;

    private String especialidade;
    private String local;
    private Integer medicoId;
    private Integer pacienteId;

    private Integer situacaoId;
    private String telefoneConsultorio;
    private BigDecimal valorConsulta;

    public AgendamentoDTO(Agendamento agendamento) {
        this.agendamentoId = agendamento.getAgendamentoId();
        this.data = agendamento.getData();
        this.especialidade = agendamento.getEspecialidade();
        this.local = agendamento.getLocal();
        this.medicoId = agendamento.getMedico().getId();
        this.pacienteId = agendamento.getPaciente().getPacienteId(); // Corrigido para .getId()
        this.situacaoId = agendamento.getSituacao().getSituacaoId();
        this.telefoneConsultorio = agendamento.getTelefoneConsultorio();
        this.valorConsulta = agendamento.getValorConsulta();
    }
}
