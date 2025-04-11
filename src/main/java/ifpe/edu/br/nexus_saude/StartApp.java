package ifpe.edu.br.nexus_saude;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.repository.AdministradorRepository;

@Component
public class StartApp implements CommandLineRunner{
	@Autowired
	private AdministradorRepository repository;
	@Override
	public void run(String... args) throws Exception {
		
		Administrador administrador = new Administrador();
		administrador.setEmail("testandoAppNexus@gmail.com");
		administrador.setSenha("senha123");
		
		//repository.save(administrador);
		//System.out.println("Administrador salvo com sucesso: " + administrador);
		
	}

}
