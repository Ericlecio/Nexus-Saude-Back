package ifpe.edu.br.nexus_saude.dto;

import java.time.LocalDateTime;

import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiasAtendimentoDTO {
	private Integer diasAtendimentoId;
    private String nomeMedico; // Assuming Medico has a 'nome' field
    private String diaSemana;
    private String horario;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public DiasAtendimentoDTO(DiasAtendimento dia) {
        this.diasAtendimentoId = dia.getDiasAtendimentoId();
        this.nomeMedico = dia.getMedico().getNome();
        this.diaSemana = dia.getDiaSemana();
        this.horario = dia.getHorario();
        this.createdAt = dia.getCreatedAt();
        this.updatedAt = dia.getUpdatedAt();
    }


}
