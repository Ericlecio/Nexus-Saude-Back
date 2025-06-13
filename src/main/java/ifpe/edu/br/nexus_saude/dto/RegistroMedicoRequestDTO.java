package ifpe.edu.br.nexus_saude.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroMedicoRequestDTO extends RegistroUsuarioRequestDTO {
	@NotBlank(message = "O nome do médico é obrigatório.")
    private String nome;

    @NotBlank(message = "O CRM é obrigatório.")
    private String crm;

    @NotBlank(message = "A especialidade é obrigatória.")
    private String especialidade;

    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "O sexo é obrigatório.")
    private String sexo; // "M" ou "F" por exemplo

    // Adicione outros campos do Medico que são preenchidos no registro
    // Ex: private LocalDate dataNascimento;
}
