package ifpe.edu.br.nexus_saude.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class RegistroMedicoRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    @NotBlank
    private String especialidade;

    @NotBlank
    private String crm;

    @NotBlank
    private String cpf;

    @NotBlank
    private String sexo;

    @NotBlank
    private String telefoneConsultorio;

    @NotNull
    private Integer tempoConsulta;

    @NotBlank
    private String uf;

    @NotNull
    private BigDecimal valorConsulta;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;


    private List<DiasAtendimentoDTO> diasAtendimento;
}

