package ifpe.edu.br.nexus_saude.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.dto.AdministradorDTO;
import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.repository.AdministradorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
public class AdministradorController {
	@Autowired
	private AdministradorRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@PostMapping("/admin")
	public ResponseEntity<AdministradorDTO> postAdmin(@RequestBody Administrador admin){
		admin.setSenha(passwordEncoder.encode(admin.getSenha()));
		Administrador save = repository.save(admin);
		return ResponseEntity.status(HttpStatus.CREATED).body(new AdministradorDTO(save));
		
	}
	@GetMapping("/admin/listar")
	public List<AdministradorDTO> getAdmins(){
		return repository.findAll()
				.stream()
				.map(AdministradorDTO::new)
				.toList();
	}
}
