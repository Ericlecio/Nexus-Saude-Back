package ifpe.edu.br.nexus_saude.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;

public interface SituacaoAgendamentoRepository extends JpaRepository<SituacaoAgendamento, Integer> {
}
