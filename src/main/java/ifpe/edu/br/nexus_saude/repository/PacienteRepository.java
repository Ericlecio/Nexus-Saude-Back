package ifpe.edu.br.nexus_saude.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ifpe.edu.br.nexus_saude.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
	 boolean existsByCpf(String cpf);
}
