package ifpe.edu.br.nexus_saude.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.repository.AdministradorRepository;

@RestController
public class AdministradorController {
	@Autowired
	private AdministradorRepository repository;
	
	@PostMapping("/admin")
	public void postAdmin(@RequestBody Administrador admin) {
		repository.save(admin);
	}
	@GetMapping("/admin/listar")
	public List<Administrador> getAdmins(){
		return repository.findAll();
	}
}
