package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.dto.RegistroMedicoRequestDTO;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Papel;
import ifpe.edu.br.nexus_saude.model.Usuario;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PapelRepository;
import ifpe.edu.br.nexus_saude.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/medico")
public class MedicoController {
	@Autowired private MedicoRepository medicoRepository;
	@Autowired private UsuarioRepository usuarioRepository;
	@Autowired private PapelRepository papelRepository;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private DiasAtendimentoRepository diasAtendimentoRepository; // Já existia

	@PostMapping("/registrar") // Endpoint para auto-registro de médico ou por admin
	// @PreAuthorize("permitAll()") // Se for auto-registro
	@PreAuthorize("hasRole('ADMIN')") // Se apenas admin pode registrar médico
	public ResponseEntity<?> registrarMedico(@Valid @RequestBody RegistroMedicoRequestDTO requestDTO) {
		if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
			return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
		}
		if (medicoRepository.existsByCrm(requestDTO.getCrm())) { // Adicionar este método ao MedicoRepository
			return ResponseEntity.badRequest().body("Erro: CRM já está em uso!");
		}
		if (medicoRepository.existsByCpf(requestDTO.getCpf())) { // Adicionar este método ao MedicoRepository
			return ResponseEntity.badRequest().body("Erro: CPF já está em uso!");
		}


		Usuario usuario = new Usuario(requestDTO.getEmail(), passwordEncoder.encode(requestDTO.getSenha()));
		Papel medicoPapel = papelRepository.findByNome("MEDICO")
				.orElseGet(() -> papelRepository.save(new Papel("MEDICO")));
		usuario.setPapeis(Set.of(medicoPapel));

		Medico medico = new Medico();
		medico.setUsuario(usuario);
		medico.setNome(requestDTO.getNome());
		medico.setCrm(requestDTO.getCrm());
		medico.setEspecialidade(requestDTO.getEspecialidade());
		medico.setCpf(requestDTO.getCpf());
		medico.setSexo(requestDTO.getSexo());
		// Configure outros campos do medico a partir do DTO (dataNascimento, telefoneConsultorio, etc.)

		Medico savedMedico = medicoRepository.save(medico);
		// Retornar DTO de resposta
		return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoDTO(savedMedico));
	}
	@GetMapping("/listar")
	@PreAuthorize("hasRole('ADMIN')") 
	public List<MedicoDTO> getMedicos() {
		return medicoRepository.findAll()
				.stream()
				.map(MedicoDTO::new) // O MedicoDTO precisa ser ajustado para pegar email do Usuario
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')") // Admin ou o próprio médico
	public ResponseEntity<MedicoDTO> getMedicoById(@PathVariable Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName(); // email do usuário logado

		return medicoRepository.findById(id)
				.map(medico -> {
					// Se o usuário logado não é ADMIN, verificar se é o próprio médico
					boolean isAdmin = authentication.getAuthorities().stream()
							.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
					if (!isAdmin && !medico.getUsuario().getEmail().equals(currentPrincipalName)) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).<MedicoDTO>build();
					}
					return ResponseEntity.ok(new MedicoDTO(medico));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteMedico1(@PathVariable Integer id) {
		return repository.findById(id)
				.map(medico -> {
					repository.delete(medico);
					return ResponseEntity.noContent().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
	public ResponseEntity<MedicoDTO> updateMedico(@PathVariable Integer id, @Valid @RequestBody RegistroMedicoRequestDTO medicoAtualizadoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName(); // email

		return medicoRepository.findById(id)
				.map(medico -> {
					boolean isAdmin = authentication.getAuthorities().stream()
							.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
					if (!isAdmin && !medico.getUsuario().getEmail().equals(currentPrincipalName)) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).<MedicoDTO>build();
					}

					Usuario usuario = medico.getUsuario();
					// Atualizar email apenas se fornecido, diferente e não existente
					if (medicoAtualizadoDTO.getEmail() != null && !medicoAtualizadoDTO.getEmail().equalsIgnoreCase(usuario.getEmail())) {
						if (usuarioRepository.existsByEmail(medicoAtualizadoDTO.getEmail())) {
							// Lançar exceção ou retornar BadRequest
							throw new RuntimeException("Email já em uso.");
						}
						usuario.setEmail(medicoAtualizadoDTO.getEmail());
					}
					if (medicoAtualizadoDTO.getSenha() != null && !medicoAtualizadoDTO.getSenha().isEmpty()) {
						usuario.setSenha(passwordEncoder.encode(medicoAtualizadoDTO.getSenha()));
					}

					medico.setNome(medicoAtualizadoDTO.getNome());
					// medico.setCrm(medicoAtualizadoDTO.getCrm()); // CRM geralmente não muda, ou requer validação especial
					medico.setEspecialidade(medicoAtualizadoDTO.getEspecialidade());
					// Outros campos atualizáveis
					// medico.setTelefoneConsultorio(medicoAtualizadoDTO.getTelefoneConsultorio());


					Medico updated = medicoRepository.save(medico);
					return ResponseEntity.ok(new MedicoDTO(updated));
				})
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/deletar/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas Admin pode deletar médicos
    public ResponseEntity<Object> deleteMedico(@PathVariable Integer id) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    medicoRepository.delete(medico);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
