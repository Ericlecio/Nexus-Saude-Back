package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.model.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdministradorDTO {
    private Integer id;
    private String email;
    private Long usuarioId;

    // Construtor que recebe a entidade Administrador
    public AdministradorDTO(Administrador admin) {
        this.id = admin.getId(); //
        if (admin.getUsuario() != null) {
            this.email = admin.getUsuario().getEmail();
            this.usuarioId = admin.getUsuario().getId();
        }
    }

    public AdministradorDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}