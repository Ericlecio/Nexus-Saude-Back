package ifpe.edu.br.nexus_saude.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ifpe.edu.br.nexus_saude.repository.UsuarioRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	 @Autowired
	    private UsuarioRepository usuarioRepository;

	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        return usuarioRepository.findByEmail(email)
	                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + email));
	    }
}
