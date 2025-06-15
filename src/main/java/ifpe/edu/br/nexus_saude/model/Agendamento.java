package ifpe.edu.br.nexus_saude.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer agendamentoId;

    private LocalDateTime data;
    private String especialidade;
    private String local;

    @ManyToOne
    private Medico medico;

    @ManyToOne
    private Paciente paciente;

    @ManyToOne
    private SituacaoAgendamento situacao;

    private String telefoneConsultorio;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorConsulta;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
