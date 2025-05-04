package ifpe.edu.br.nexus_saude.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ifpe.edu.br.nexus_saude.model.LogAlteracoes;
@Repository
public interface LogAlteracoesRepository extends JpaRepository<LogAlteracoes, Integer>{

}
