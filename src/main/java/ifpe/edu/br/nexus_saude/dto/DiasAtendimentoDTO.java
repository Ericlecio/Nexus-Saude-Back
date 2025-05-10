package ifpe.edu.br.nexus_saude.dto;

import java.time.LocalDateTime;

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

}
