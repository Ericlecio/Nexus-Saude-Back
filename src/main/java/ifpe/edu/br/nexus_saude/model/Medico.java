package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    @Column(length = 100, nullable = false)
    private String nome;

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

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DiasAtendimento> diasAtendimento;

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
        return "Medico [id=" + id + ", nome=" + nome + (usuario != null ? ", email=" + usuario.getEmail() : "") +
                ", crm=" + crm + ", especialidade=" + especialidade + "]";
    }
}
