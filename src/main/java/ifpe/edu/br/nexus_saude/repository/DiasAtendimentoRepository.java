package ifpe.edu.br.nexus_saude.repository;

import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiasAtendimentoRepository extends JpaRepository<DiasAtendimento, Integer> {
    List<DiasAtendimento> findByMedicoId(Integer medicoId);

}
