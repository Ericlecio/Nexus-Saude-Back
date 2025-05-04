package ifpe.edu.br.nexus_saude.dto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoDTO{
	private Integer id;
    private LocalDateTime data;
    private String especialidade;
    private String local;
    private Integer medicoId;
    private Integer pacienteId;
    private Integer situacaoId;
    private String telefoneConsultorio;
    private BigDecimal valorConsulta;
}