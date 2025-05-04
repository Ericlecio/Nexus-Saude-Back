package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "log_alteracoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogAlterações {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(nullable = false, length = 100)
    private String entidade;

    @Column(nullable = false)
    private String registroId;

    @Column(nullable = false, length = 100)
    private String usuarioResponsavel;

    @Column(nullable = false)
    private String dataAlteracao;

    @Column(nullable = false)
    private String alteracao;
}
