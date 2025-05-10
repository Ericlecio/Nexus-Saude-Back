package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SituacaoAgendamentoDTO {
	private Integer situacaoId;
    private String descricao;

    public SituacaoAgendamento toEntity() {
        return SituacaoAgendamento.builder()
                .situacaoId(this.situacaoId)
                .descricao(this.descricao)
                .build();
    }

}
