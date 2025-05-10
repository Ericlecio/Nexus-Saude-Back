package ifpe.edu.br.nexus_saude.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/medico")
public class MedicoController {

	@Autowired
	private MedicoRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/inserir")
	public ResponseEntity<MedicoDTO> postMedico(@RequestBody Medico medico) {
		medico.setSenha(passwordEncoder.encode(medico.getSenha()));
		medico.setEmail(medico.getEmail().toLowerCase());
		Medico savedMedico = repository.save(medico);
		return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoDTO(savedMedico));
	}

	@GetMapping("/listar")
	public List<MedicoDTO> getMedicos() {
		return repository.findAll()
				.stream()
				.map(MedicoDTO::new)
				.toList();
	}

	@PutMapping("/{id}")
	public ResponseEntity<MedicoDTO> updateMedico(@PathVariable Integer id, @RequestBody Medico medicoAtualizado) {
		return repository.findById(id)
				.map(medico -> {
					medico.setNome(medicoAtualizado.getNome());
					medico.setEmail(medicoAtualizado.getEmail());
					medico.setSenha(passwordEncoder.encode(medicoAtualizado.getSenha()));
					medico.setCrm(medicoAtualizado.getCrm());
					medico.setEspecialidade(medicoAtualizado.getEspecialidade());
					Medico updated = repository.save(medico);
					return ResponseEntity.ok(new MedicoDTO(updated));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteMedico(@PathVariable Integer id) {
		return repository.findById(id)
				.map(medico -> {
					repository.delete(medico);
					return ResponseEntity.noContent().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}
}
