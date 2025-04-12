package ifpe.edu.br.nexus_saude.dto;

import ifpe.edu.br.nexus_saude.model.Medico;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDTO {
    private Integer id;
    private String nome;
    private String email;
    private String crm;
    private String especialidade;

    public MedicoDTO(Medico medico) {
        this.id = medico.getId();
        this.nome = medico.getNome();
        this.email = medico.getEmail();
        this.crm = medico.getCrm();
        this.especialidade = medico.getEspecialidade();
    }
}
