package ifpe.edu.br.nexus_saude.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "consultas")
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consulta_id")
	private Integer consultaId;

	@ManyToOne
	private Paciente paciente;

	@ManyToOne
	private Medico medico;

	@Column(nullable = false)
	private LocalDateTime data; // Alterado de String para LocalDateTime

	@Column(nullable = false)
	private String especialidade;

	@Column(nullable = false)
	private String local;

	@ManyToOne
	private SituacaoAgendamento situacao;

	@Column(nullable = false)
	private String createdAt;

	@Column(nullable = false)
	private String updatedAt;
}
