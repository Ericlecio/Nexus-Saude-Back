package ifpe.edu.br.nexus_saude.dto;

import java.time.LocalDateTime;

import ifpe.edu.br.nexus_saude.model.LogAlteracoes;
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

public class LogAlteracoesDTO {
    private Integer logId;
    private String entidade;
    private String registroId;
    private String usuarioResponsavel;
    private LocalDateTime dataAlteracao;
    private String alteracao;

    public static LogAlteracoesDTO fromEntity(LogAlteracoes log) {
        return LogAlteracoesDTO.builder()
                .logId(log.getLogId())
                .entidade(log.getEntidade())
                .registroId(log.getRegistroId())
                .usuarioResponsavel(log.getUsuarioResponsavel())
                .dataAlteracao(log.getDataAlteracao())
                .alteracao(log.getAlteracao())
                .build();
    }

}
