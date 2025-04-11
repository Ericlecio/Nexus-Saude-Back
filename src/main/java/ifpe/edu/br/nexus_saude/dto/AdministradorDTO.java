package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Administrador;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdministradorDTO {
	private Integer id;
	private String email;
	
	public AdministradorDTO(Administrador admin) {
		this.id = admin.getId();
		this.email = admin.getEmail();
	}
}
