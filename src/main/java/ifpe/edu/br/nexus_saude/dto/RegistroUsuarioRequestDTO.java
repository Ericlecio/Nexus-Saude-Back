package ifpe.edu.br.nexus_saude.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroUsuarioRequestDTO {

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String senha;
}
