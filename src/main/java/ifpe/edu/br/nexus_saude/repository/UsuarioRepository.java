package ifpe.edu.br.nexus_saude.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ifpe.edu.br.nexus_saude.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
