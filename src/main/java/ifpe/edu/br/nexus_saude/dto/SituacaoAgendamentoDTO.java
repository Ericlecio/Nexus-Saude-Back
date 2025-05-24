package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SituacaoAgendamentoDTO {
    private Integer situacaoId;
    private String descricao;

    public SituacaoAgendamentoDTO(SituacaoAgendamento situacao) {
        this.situacaoId = situacao.getSituacaoId();
        this.descricao = situacao.getDescricao();
    }

    public SituacaoAgendamento toEntity() {
        return SituacaoAgendamento.builder()
                .situacaoId(this.situacaoId)
                .descricao(this.descricao)
                .build();
    }
}
