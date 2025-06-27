package ifpe.edu.br.nexus_saude.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoUpdateProfileDTO {
	
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres.")
    private String email;

    
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    
    @Size(max = 20, message = "O CRM deve ter no máximo 20 caracteres.")
    private String crm; 

    @Size(max = 50, message = "A especialidade deve ter no máximo 50 caracteres.")
    private String especialidade;

    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    private String cpf; 

    @Size(max = 1, message = "O sexo deve ter no máximo 1 caracter.")
    private String sexo;

    @Size(max = 20, message = "O telefone do consultório deve ter no máximo 20 caracteres.")
    private String telefoneConsultorio;

    @Min(value = 1, message = "O tempo de consulta deve ser no mínimo 1 minuto.")
    private Integer tempoConsulta;

    @Size(max = 2, message = "A UF deve ter no máximo 2 caracteres.")
    private String uf;

    @DecimalMin(value = "0.0", inclusive = false, message = "O valor da consulta deve ser maior que zero.")
    @Digits(integer = 8, fraction = 2, message = "O valor da consulta deve ter até 8 dígitos inteiros e 2 decimais.")
    private BigDecimal valorConsulta;

    @Past(message = "A data de nascimento deve ser uma data no passado.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    private List<DiasAtendimentoDTO> diasAtendimento;
}
