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
    @Column(name = "id") 
    private Integer pacienteId;

    
    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;
    
    @Column(length = 100, nullable = false)
    private String nomeCompleto;

   // email e senha estão na class usuario

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
        this.dataCadastro = LocalDateTime.now(); // dataCadastro já existe e é setado aqui
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Paciente [pacienteId=" + pacienteId + ", nomeCompleto=" + nomeCompleto + (usuario != null ? ", email=" + usuario.getEmail() : "") +
                ", cpf=" + cpf + ", planoSaude=" + planoSaude + "]";
    }
}
