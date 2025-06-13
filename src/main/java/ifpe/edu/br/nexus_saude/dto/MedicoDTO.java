package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Usuario; // Importe a entidade Usuario
import lombok.*; // Manteve Getter, Setter, NoArgsConstructor, AllArgsConstructor
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors; // Para mapear DiasAtendimento para DTOs, se necessário

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Mantido, embora o construtor principal seja o que recebe a entidade Medico
public class MedicoDTO {
    private Integer id;
    private String nome;
    private String email; // Virá do Usuario associado
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
    private Long usuarioId; // Opcional: se você quiser expor o ID do usuário associado

    // Se DiasAtendimento também tiver um DTO, use-o aqui.
    // Por enquanto, mantendo a entidade, mas idealmente seria List<DiasAtendimentoDTO>
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
        this.dataCadastro = medico.getDataCadastro(); // Este campo já existe em Medico
        this.createdAt = medico.getCreatedAt(); // Este campo já existe em Medico
        this.updatedAt = medico.getUpdatedAt(); // Este campo já existe em Medico

        // Mapeamento do email e usuarioId a partir da entidade Usuario associada
        if (medico.getUsuario() != null) {
            this.email = medico.getUsuario().getEmail();
            this.usuarioId = medico.getUsuario().getId(); // Opcional
        }

        // Mapear DiasAtendimento para DiasAtendimentoDTO
        // (Assumindo que você tenha um DiasAtendimentoDTO similar ao que foi fornecido antes)
        if (medico.getDiasAtendimento() != null) {
            this.diasAtendimento = medico.getDiasAtendimento().stream()
                    .map(dia -> new DiasAtendimentoDTO( // Utilizando o construtor de DiasAtendimentoDTO que você já possui
                            dia.getDiasAtendimentoId(),
                            dia.getMedico().getNome(), // Pode ser redundante se o MedicoDTO já tem o nome
                            dia.getDiaSemana(),
                            dia.getHorario(),
                            dia.getCreatedAt(),
                            dia.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());
        }

    }

}
