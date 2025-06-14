package ifpe.edu.br.nexus_saude.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroPacienteRequestDTO extends RegistroUsuarioRequestDTO {

    @NotBlank(message = "O nome completo do paciente é obrigatório.")
    @Size(max = 100, message = "O nome completo deve ter no máximo 100 caracteres.")
    private String nomeCompleto;

    @NotBlank(message = "O telefone é obrigatório.")
    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    private String telefone;

    @NotBlank(message = "O CPF é obrigatório.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)

    @Size(max = 50, message = "O plano de saúde deve ter no máximo 50 caracteres.")
    private String planoSaude;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
}