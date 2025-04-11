package ifpe.edu.br.nexus_saude.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Administrador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 50, nullable = false)
	private String email;
	@Column(length  = 100, nullable = false)
	private String senha;
	
	@Override
	public String toString() {
		return "Administrador [id=" + id + ", email=" + email + ", senha=" + senha + "]";
	}
	
}
