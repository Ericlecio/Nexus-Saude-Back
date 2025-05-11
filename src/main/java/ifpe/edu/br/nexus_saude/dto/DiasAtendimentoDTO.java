package ifpe.edu.br.nexus_saude.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiasAtendimentoDTO {

    private Integer diasAtendimentoId;
    private String nomeMedico;
    private String diaSemana;
    private String horario;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
