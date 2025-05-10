package ifpe.edu.br.nexus_saude.dto;

import java.math.BigDecimal;
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
public class ConsultaHistoricoDTO {
	private Integer id;
    private LocalDateTime data;
    private LocalDateTime dataAtualizacao;
    private String especialidade;
    private String local;
    private String nomeMedico; // Extracting only the name
    private String nomePaciente; // Extracting only the name
    private String situacao;
    private String telefoneConsultorio;
    private BigDecimal valorConsulta;


}
