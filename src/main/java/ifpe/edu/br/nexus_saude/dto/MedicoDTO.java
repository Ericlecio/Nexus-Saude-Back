package ifpe.edu.br.nexus_saude.dto;

// import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
// import ifpe.edu.br.nexus_saude.model.Usuario;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private Long usuarioId;

    private List<DiasAtendimentoDTO> diasAtendimento;

    public MedicoDTO(Medico medico) {
        this.id = medico.getId();
        this.nome = medico.getNome();
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

        if (medico.getUsuario() != null) {
            this.email = medico.getUsuario().getEmail();
            this.usuarioId = medico.getUsuario().getId();
        }

        if (medico.getDiasAtendimento() != null) {
            this.diasAtendimento = medico.getDiasAtendimento().stream()
                    .map(dia -> new DiasAtendimentoDTO(
                            dia.getDiasAtendimentoId(),
                            dia.getMedico().getNome(),
                            dia.getDiaSemana(),
                            dia.getHorario(),
                            dia.getCreatedAt(),
                            dia.getUpdatedAt()))
                    .collect(Collectors.toList());
        }
    }
}