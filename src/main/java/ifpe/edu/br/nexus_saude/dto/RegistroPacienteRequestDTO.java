package ifpe.edu.br.nexus_saude.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat; // Para formatação de data

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPacienteRequestDTO extends RegistroUsuarioRequestDTO { // Garante a herança

    @NotBlank(message = "O nome completo do paciente é obrigatório.")
    @Size(max = 100, message = "O nome completo deve ter no máximo 100 caracteres.")
    private String nomeCompleto;

    @NotBlank(message = "O telefone é obrigatório.")
    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    // Você pode adicionar uma @Pattern para validar o formato do telefone se desejar
    // Ex: @Pattern(regexp = "^[0-9]{10,15}$", message = "Formato de telefone inválido.")
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    // Para validar o CPF, você pode usar @Pattern ou uma biblioteca específica de validação de CPF
    // Ex: @Pattern(regexp = "^[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}$", message = "Formato de CPF inválido.")
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Espera o formato YYYY-MM-DD
    private LocalDate dataNascimento;

    @Size(max = 50, message = "O plano de saúde deve ter no máximo 50 caracteres.")
    private String planoSaude; // Opcional, então não tem @NotBlank

    // Os campos 'email' e 'senha' são herdados de RegistroUsuarioRequestDTO:
    // @NotBlank(message = "O email é obrigatório.")
    // @Email(message = "Formato de email inválido.")
    // private String email;

    // @NotBlank(message = "A senha é obrigatória.")
    // @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    // private String senha;
}