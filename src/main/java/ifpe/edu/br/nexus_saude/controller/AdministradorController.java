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


import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class AdministradorController {
	@Autowired
	private AdministradorRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@PostMapping("/admin")
	public ResponseEntity<AdministradorDTO> postAdmin(@RequestBody Administrador admin) {
		admin.setSenha(passwordEncoder.encode(admin.getSenha()));
		Administrador savedAdmin = repository.save(admin);
		return ResponseEntity.status(HttpStatus.CREATED).body(new AdministradorDTO(savedAdmin));
	}
	@GetMapping("/admin/listar")
	public List<AdministradorDTO> getAdmins(){
		return repository.findAll()
				.stream()
				.map(AdministradorDTO::new)
				.toList();
	}

//Atualizar um administrador
@PutMapping("/admin/{id}")
public ResponseEntity<AdministradorDTO> updateAdmin(@PathVariable Integer id, @RequestBody Administrador adminDetails) {
	return repository.findById(id)
			.map(admin -> {
				admin.setEmail(adminDetails.getEmail());
				if (adminDetails.getSenha() != null && !adminDetails.getSenha().isEmpty()) {
					admin.setSenha(passwordEncoder.encode(adminDetails.getSenha()));
				}
				Administrador updatedAdmin = repository.save(admin);
				return ResponseEntity.ok(new AdministradorDTO(updatedAdmin));
			})
			.orElse(ResponseEntity.notFound().build());
}

//Deletar um administrador
@DeleteMapping("/admin/{id}")
public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
	return repository.findById(id)
			.map(admin -> {
				repository.delete(admin);
				return ResponseEntity.noContent().<Void>build();
			})
			.orElse(ResponseEntity.notFound().build());
}}