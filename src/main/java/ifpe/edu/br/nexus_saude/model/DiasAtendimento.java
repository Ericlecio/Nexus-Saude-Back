package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "dias_atendimento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiasAtendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dias_atendimento_id")
    private Integer diasAtendimentoId;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    @JsonBackReference
    private Medico medico;

    @Column(nullable = false, length = 20)
    private String diaSemana;

    @Column(nullable = false)
    private String horario;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
