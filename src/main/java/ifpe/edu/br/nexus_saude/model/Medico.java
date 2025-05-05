package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String senha;

    @Column(length = 20, nullable = false, unique = true)
    private String crm;

    @Column(length = 50, nullable = false)
    private String especialidade;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 1, nullable = false)
    private String sexo;

    @Column(length = 20)
    private String telefoneConsultorio;

    private Integer tempoConsulta; 

    @Column(length = 2)
    private String uf;

    @Column(precision = 10, scale = 2)
    private BigDecimal valorConsulta;

    private LocalDate dataNascimento;

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
        return "Medico [id=" + id + ", nome=" + nome + ", email=" + email +
               ", crm=" + crm + ", especialidade=" + especialidade + "]";
    }
}
