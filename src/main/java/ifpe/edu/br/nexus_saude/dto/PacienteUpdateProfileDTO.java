package ifpe.edu.br.nexus_saude.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteUpdateProfileDTO {
	 
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres.")
    private String email;

    @Size(max = 100, message = "O nome completo do paciente deve ter no máximo 100 caracteres.")
    private String nomeCompleto;

    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    private String telefone;

    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    private String cpf; // Consider making this non-updatable if it's truly immutable

    @Size(max = 50, message = "O plano de saúde deve ter no máximo 50 caracteres.")
    private String planoSaude;

    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
}
