package ifpe.edu.br.nexus_saude.repository;

import ifpe.edu.br.nexus_saude.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Integer> {

	boolean existsByCrm(String crm);
}
