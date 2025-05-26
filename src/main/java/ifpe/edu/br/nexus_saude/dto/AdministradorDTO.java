package ifpe.edu.br.nexus_saude.dto;

// import ifpe.edu.br.nexus_saude.model.Administrador; // Não é mais usado no construtor direto assim

import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.model.Usuario; // Importe a entidade Usuario
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor // Adicionado para flexibilidade e consistência
public class AdministradorDTO {
    private Integer id;
    private String email; // Virá do Usuario associado
    private Long usuarioId; // Opcional: se você quiser expor o ID do usuário associado

    // Construtor que recebe a entidade Administrador
    public AdministradorDTO(Administrador admin) {
        this.id = admin.getId(); //
        if (admin.getUsuario() != null) {
            this.email = admin.getUsuario().getEmail();
            this.usuarioId = admin.getUsuario().getId(); // Opcional
        }
    }

    // Construtor alternativo, caso você precise criar o DTO com id e email diretamente
    // (como usado no exemplo do AdministradorController)
    public AdministradorDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}