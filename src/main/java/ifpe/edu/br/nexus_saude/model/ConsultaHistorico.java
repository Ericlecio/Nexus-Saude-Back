package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "consulta_historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaHistorico {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consulta_historico_id")
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime data;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @Column(length = 100, nullable = false)
    private String especialidade;

    @Column(length = 100, nullable = false)
    private String local;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "situacao_id", nullable = false)
    private SituacaoAgendamento situacao;

    @Column(length = 20)
    private String telefoneConsultorio;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorConsulta;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
}
