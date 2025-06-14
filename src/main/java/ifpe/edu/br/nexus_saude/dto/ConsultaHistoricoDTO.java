package ifpe.edu.br.nexus_saude.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ifpe.edu.br.nexus_saude.model.ConsultaHistorico;
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
    private String nomeMedico;
    private String nomePaciente;
    private String situacao;
    private String telefoneConsultorio;
    private BigDecimal valorConsulta;

    public ConsultaHistoricoDTO(ConsultaHistorico consulta) {
        this.id = consulta.getId();
        this.data = consulta.getData();
        this.dataAtualizacao = consulta.getDataAtualizacao();
        this.especialidade = consulta.getEspecialidade();
        this.local = consulta.getLocal();
        this.nomeMedico = consulta.getMedico().getNome();
        this.nomePaciente = consulta.getPaciente().getNomeCompleto();
        this.situacao = consulta.getSituacao().getDescricao();
        this.telefoneConsultorio = consulta.getTelefoneConsultorio();
        this.valorConsulta = consulta.getValorConsulta();
    }
}