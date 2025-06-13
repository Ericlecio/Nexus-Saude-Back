package ifpe.edu.br.nexus_saude.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ifpe.edu.br.nexus_saude.model.Papel;

public interface PapelRepository  extends JpaRepository<Papel, Integer> {
	Optional<Papel> findByNome(String nome);
}
