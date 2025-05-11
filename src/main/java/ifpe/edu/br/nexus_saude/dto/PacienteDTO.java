package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Paciente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate; // Alterado para LocalDate
import java.time.LocalDateTime;

@Getter
@Setter
public class PacienteDTO {
    private Integer pacienteId;
    private String nomeCompleto;
    private String email;
    private String telefone;
    private String cpf;
    private LocalDate dataNascimento; // Alterado para LocalDate
    private String planoSaude;
    private LocalDateTime dataCadastro;
    private LocalDateTime updatedAt;

    // Construtor que recebe um Paciente e mapeia os campos
    public PacienteDTO(Paciente paciente) {
        this.id = paciente.getPacienteId(); // Alterado de getId() para getPacienteId()
        this.nomeCompleto = paciente.getNomeCompleto();
        this.email = paciente.getEmail();
        this.telefone = paciente.getTelefone();
        this.cpf = paciente.getCpf();
        this.dataNascimento = paciente.getDataNascimento(); // Adicionado
        this.planoSaude = paciente.getPlanoSaude();
        this.dataCadastro = paciente.getDataCadastro();
        this.updatedAt = paciente.getUpdatedAt();
    }
}
