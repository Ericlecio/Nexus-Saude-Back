package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Paciente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PacienteDTO {
	private Integer id; // Mantido como 'id' para consistência com o DTO, mas refere-se a pacienteId na entidade

    private Integer id;
    private String nomeCompleto;
    private String email; // Virá do Usuario associado
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String planoSaude;
    private LocalDateTime dataCadastro;
    private LocalDateTime updatedAt;
    private Long usuarioId; // Opcional: se você quiser expor o ID do usuário associado

    public PacienteDTO(Paciente paciente) {
        this.id = paciente.getPacienteId();
        this.nomeCompleto = paciente.getNomeCompleto();
        this.telefone = paciente.getTelefone();
        this.cpf = paciente.getCpf();
        this.dataNascimento = paciente.getDataNascimento();
        this.planoSaude = paciente.getPlanoSaude();
        this.dataCadastro = paciente.getDataCadastro(); // Este campo já existe em Paciente
        this.updatedAt = paciente.getUpdatedAt(); // Este campo já existe em Paciente

        // Mapeamento do email e usuarioId a partir da entidade Usuario associada
        if (paciente.getUsuario() != null) {
            this.email = paciente.getUsuario().getEmail();
            this.usuarioId = paciente.getUsuario().getId(); // Opcional
        }
    }
}
