package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "situacao_agendamento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SituacaoAgendamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "situacao_id")
	private Integer situacaoId;

	@Column(nullable = false)
	private String descricao;
}
