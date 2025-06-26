package ifpe.edu.br.nexus_saude.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ifpe.edu.br.nexus_saude.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {
	boolean existsByCrm(String crm);

	boolean existsByCpf(String cpf);

	// Método necessário para identificar o médico pelo usuário logado
	Optional<Medico> findByUsuarioId(Long usuarioId);
}
