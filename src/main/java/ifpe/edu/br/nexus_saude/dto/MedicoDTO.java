package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDTO {
    private Integer id;
    private String nome;
    private String email;
    private String crm;
    private String especialidade;
    private String cpf;
    private String sexo;
    private String telefoneConsultorio;
    private Integer tempoConsulta;
    private String uf;
    private BigDecimal valorConsulta;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DiasAtendimento> diasAtendimento; 

    public MedicoDTO(Medico medico) {
        this.id = medico.getId();
        this.nome = medico.getNome();
        this.email = medico.getEmail();
        this.crm = medico.getCrm();
        this.especialidade = medico.getEspecialidade();
        this.cpf = medico.getCpf();
        this.sexo = medico.getSexo();
        this.telefoneConsultorio = medico.getTelefoneConsultorio();
        this.tempoConsulta = medico.getTempoConsulta();
        this.uf = medico.getUf();
        this.valorConsulta = medico.getValorConsulta();
        this.dataNascimento = medico.getDataNascimento();
        this.dataCadastro = medico.getDataCadastro();
        this.createdAt = medico.getCreatedAt();
        this.updatedAt = medico.getUpdatedAt();

    }
}
