package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Paciente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PacienteDTO {
    private Integer id;
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento;
    private String planoSaude;
    private LocalDateTime dataCadastro;
    private LocalDateTime updatedAt;

    public PacienteDTO(Paciente paciente) {
        this.id = paciente.getPacienteId();
        this.nomeCompleto = paciente.getNomeCompleto();
        this.email = paciente.getEmail();
        this.telefone = paciente.getTelefone();
        this.cpf = paciente.getCpf();
        this.dataNascimento = paciente.getDataNascimento();
        this.planoSaude = paciente.getPlanoSaude();
        this.dataCadastro = paciente.getDataCadastro();
        this.updatedAt = paciente.getUpdatedAt();
    }
}
