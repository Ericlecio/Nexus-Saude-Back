package ifpe.edu.br.nexus_saude.dto;

// import ifpe.edu.br.nexus_saude.model.Administrador; // Não é mais usado no construtor direto assim
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // Adicionado para o novo construtor
public class AdministradorDTO {
    private Integer id;
    private String email;
    // Adicione outros campos que você quer retornar sobre o admin

    // Construtor usado nos controllers após salvar/buscar
    public AdministradorDTO(Integer id, String email) {
        this.id = id;
        this.email = email;
    }
}