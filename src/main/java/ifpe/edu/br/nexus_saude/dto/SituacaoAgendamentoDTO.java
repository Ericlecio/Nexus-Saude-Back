package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SituacaoAgendamentoDTO {
	private Integer situacaoId;
    private String descricao;

    public SituacaoAgendamentoDTO(SituacaoAgendamento situacao) {
        this.situacaoId = situacao.getSituacaoId();
        this.descricao = situacao.getDescricao();
    }

    public SituacaoAgendamento toEntity() {
        SituacaoAgendamento situacao = new SituacaoAgendamento();
        situacao.setSituacaoId(this.situacaoId);
        situacao.setDescricao(this.descricao);
        return situacao;
    }
}
