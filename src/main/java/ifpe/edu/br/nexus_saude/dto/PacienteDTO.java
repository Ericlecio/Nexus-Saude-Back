package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Paciente;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private String pacienteId;
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String planoSaude;
    private LocalDateTime dataCadastro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PacienteDTO(Paciente paciente) {
        this.pacienteId = paciente.getPacienteId();
        this.nomeCompleto = paciente.getNomeCompleto();
        this.email = paciente.getEmail();
        this.telefone = paciente.getTelefone();
        this.cpf = paciente.getCpf();
        this.dataNascimento = paciente.getDataNascimento();
        this.planoSaude = paciente.getPlanoSaude();
        this.dataCadastro = paciente.getDataCadastro();
        this.createdAt = paciente.getCreatedAt();
        this.updatedAt = paciente.getUpdatedAt();
    }
}
