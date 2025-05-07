package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nomeCompleto;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 15, nullable = false)
    private String telefone;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    private LocalDate dataNascimento;

    @Column(length = 50)
    private String planoSaude;

    private LocalDateTime dataCadastro;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.dataCadastro = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Paciente [pacienteId=" + id + ", nomeCompleto=" + nomeCompleto + ", email=" + email +
                ", cpf=" + cpf + ", planoSaude=" + planoSaude + "]";
    }
}
