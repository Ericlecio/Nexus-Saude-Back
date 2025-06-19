package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.PacienteDTO;
import ifpe.edu.br.nexus_saude.dto.RegistroPacienteRequestDTO;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.Papel;
import ifpe.edu.br.nexus_saude.model.Usuario;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.PapelRepository;
import ifpe.edu.br.nexus_saude.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/paciente")
public class PacienteController {


	@Autowired
	private PacienteRepository pacienteRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PapelRepository papelRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/registrar")
	@PreAuthorize("permitAll()")
	public ResponseEntity<?> registrarPaciente(@Valid @RequestBody RegistroPacienteRequestDTO requestDTO) {
		return processarCadastroPaciente(requestDTO);
	}

	@PostMapping("/admin-criar")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> criarPacienteComoAdmin(@Valid @RequestBody RegistroPacienteRequestDTO requestDTO) {
		return processarCadastroPaciente(requestDTO);
	}

	private ResponseEntity<?> processarCadastroPaciente(RegistroPacienteRequestDTO requestDTO) {
		if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
			return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
		}
		if (pacienteRepository.existsByCpf(requestDTO.getCpf())) {
			return ResponseEntity.badRequest().body("Erro: CPF já está em uso!");
		}

		Usuario usuario = new Usuario(requestDTO.getEmail(), passwordEncoder.encode(requestDTO.getSenha()));
		Papel pacientePapel = papelRepository.findByNome("PACIENTE")
				.orElseGet(() -> papelRepository.save(new Papel("PACIENTE")));
		usuario.setPapeis(Set.of(pacientePapel));

		Paciente paciente = new Paciente();
		paciente.setUsuario(usuario);
		paciente.setNomeCompleto(requestDTO.getNomeCompleto());
		paciente.setTelefone(requestDTO.getTelefone());
		paciente.setCpf(requestDTO.getCpf());
		paciente.setDataNascimento(requestDTO.getDataNascimento());
		paciente.setPlanoSaude(requestDTO.getPlanoSaude());

		Paciente savedPaciente = pacienteRepository.save(paciente);

		return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteDTO(savedPaciente));
	}

	@GetMapping("/listar")
	@PreAuthorize("hasRole('ADMIN')")
	public List<PacienteDTO> getPacientes() {
		return pacienteRepository.findAll()
				.stream()
				.map(PacienteDTO::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/{pacienteId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<PacienteDTO> getPaciente(@PathVariable Integer pacienteId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		Optional<Paciente> pacienteOpt = pacienteRepository.findById(pacienteId);
		if (pacienteOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Paciente paciente = pacienteOpt.get();

		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		boolean isMedico = authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));

		if (isAdmin || isMedico || paciente.getUsuario().getEmail().equals(currentPrincipalName)) {
			return ResponseEntity.ok(new PacienteDTO(paciente));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	// em src/main/java/ifpe/edu/br/nexus_saude/controller/PacienteController.java

	@PutMapping("/update/{pacienteId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
	public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Integer pacienteId,
			@Valid @RequestBody RegistroPacienteRequestDTO pacienteAtualizadoDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		// --- ADICIONE ESTES LOGS ---
		System.out.println("\n--- INICIANDO DEBUG updatePaciente ---");
		System.out.println("ID do Paciente a ser atualizado: " + pacienteId);
		System.out.println("Usuário autenticado (do token): " + currentPrincipalName);
		System.out.println("Autoridades do usuário: " + authentication.getAuthorities());
		// --- FIM DOS LOGS INICIAIS --

		return pacienteRepository.findById(pacienteId)
				.map(paciente -> {
					boolean isAdmin = authentication.getAuthorities().stream()
							.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

					if (!isAdmin && !paciente.getUsuario().getEmail().equals(currentPrincipalName)) {
						
						return ResponseEntity.status(HttpStatus.FORBIDDEN).<PacienteDTO>build();
					}

					paciente.setNomeCompleto(pacienteAtualizadoDTO.getNomeCompleto());
					paciente.setTelefone(pacienteAtualizadoDTO.getTelefone());
					paciente.setCpf(pacienteAtualizadoDTO.getCpf());
					paciente.setDataNascimento(pacienteAtualizadoDTO.getDataNascimento());
					paciente.setPlanoSaude(pacienteAtualizadoDTO.getPlanoSaude());

					Paciente updated = pacienteRepository.save(paciente);
					return ResponseEntity.ok(new PacienteDTO(updated));

				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/deletar/{pacienteId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> deletePaciente(@PathVariable Integer pacienteId) {
		return pacienteRepository.findById(pacienteId)
				.map(paciente -> {
					pacienteRepository.delete(paciente);
					return ResponseEntity.noContent().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/redefinir-senha/{pacienteId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE')")
	public ResponseEntity<String> redefinirSenha(@PathVariable Integer pacienteId,
			@Valid @RequestBody RedefinirSenhaRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		Paciente paciente = pacienteRepository.findById(pacienteId).orElse(null);
		if (paciente == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado.");
		}

		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		if (!isAdmin && !paciente.getUsuario().getEmail().equals(currentPrincipalName)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");
		}

		if (!isAdmin) {
			if (request.getSenhaAntiga() == null || request.getSenhaAntiga().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha antiga é obrigatória.");
			}
			boolean senhaValida = passwordEncoder.matches(request.getSenhaAntiga(),
					paciente.getUsuario().getPassword());
			if (!senhaValida) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha antiga incorreta.");
			}
		}

		String novaSenhaCriptografada = passwordEncoder.encode(request.getNovaSenha());
		paciente.getUsuario().setSenha(novaSenhaCriptografada);
		pacienteRepository.save(paciente);

		return ResponseEntity.ok("Senha alterada com sucesso.");
	}

	public static class RedefinirSenhaRequest {
		private String senhaAntiga;
		private String novaSenha;

		public String getSenhaAntiga() {
			return senhaAntiga;
		}

		public void setSenhaAntiga(String senhaAntiga) {
			this.senhaAntiga = senhaAntiga;
		}

		public String getNovaSenha() {
			return novaSenha;
		}

		public void setNovaSenha(String novaSenha) {
			this.novaSenha = novaSenha;
		}
	}

	@GetMapping("/debug/my-roles")
	@PreAuthorize("isAuthenticated()")
	public Object getMyRoles(Authentication authentication) {
		return authentication.getAuthorities();
	}
}
