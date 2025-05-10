package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "medico_id", nullable = false) // Relacionando com a tabela de médico
    private Medico medico;

    @Column(nullable = false, length = 20)
    private String diaSemana; // Dia da semana

    @Column(nullable = false)
    private String horario; // Horário de atendimento

    @Column(nullable = false)
    private String createdAt;

    @Column(nullable = false)
    private String updatedAt;

    // Método setMedico
    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}
