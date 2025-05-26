package ifpe.edu.br.nexus_saude.dto;

import jakarta.validation.constraints.NotBlank;
//import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPacienteRequestDTO {
	@NotBlank(message = "O nome completo do paciente é obrigatório.")
    private String nomeCompleto;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;

    // Adicione outros campos do Paciente que são preenchidos no registro
    // Ex: private LocalDate dataNascimento;
    // private String planoSaude;
}
