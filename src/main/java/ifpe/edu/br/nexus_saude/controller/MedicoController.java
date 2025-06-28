package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.dto.MedicoUpdateProfileDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/medico")
public class MedicoController {

	@Autowired
	private MedicoRepository medicoRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private PapelRepository papelRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private DiasAtendimentoRepository diasAtendimentoRepository;

	@PostMapping("/registrar")
	public ResponseEntity<?> registrarMedico(@Valid @RequestBody RegistroMedicoRequestDTO requestDTO) {
		return processarCadastroMedico(requestDTO, false);
	}

	@PostMapping("/admin-criar")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> criarMedicoComoAdmin(@Valid @RequestBody RegistroMedicoRequestDTO requestDTO) {
		return processarCadastroMedico(requestDTO, true);
	}

	private ResponseEntity<?> processarCadastroMedico(RegistroMedicoRequestDTO requestDTO, boolean isAdmin) {
		if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
			return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
		}
		if (medicoRepository.existsByCrm(requestDTO.getCrm())) {
			return ResponseEntity.badRequest().body("Erro: CRM já está em uso!");
		}
		if (medicoRepository.existsByCpf(requestDTO.getCpf())) {
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
		medico.setDataNascimento(requestDTO.getDataNascimento());
		medico.setTelefoneConsultorio(requestDTO.getTelefoneConsultorio());
		medico.setTempoConsulta(requestDTO.getTempoConsulta());
		medico.setUf(requestDTO.getUf());
		medico.setValorConsulta(requestDTO.getValorConsulta());

		
		if (requestDTO.getDiasAtendimento() != null && !requestDTO.getDiasAtendimento().isEmpty()) {
			List<DiasAtendimento> dias = requestDTO.getDiasAtendimento().stream().map(dto -> {
				DiasAtendimento dia = new DiasAtendimento();
				dia.setMedico(medico); 
				dia.setDiaSemana(dto.getDiaSemana());
				dia.setHorario(dto.getHorario());
				dia.setCreatedAt(LocalDateTime.now());
				dia.setUpdatedAt(LocalDateTime.now());
				return dia;
			}).collect(Collectors.toList());
			medico.setDiasAtendimento(dias);
		}

		Medico savedMedico = medicoRepository.save(medico);
		return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoDTO(savedMedico));
	}

	@GetMapping("/listar")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public List<MedicoDTO> getMedicos() {
		return medicoRepository.findAll()
				.stream()
				.map(MedicoDTO::new)
				.collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
	public ResponseEntity<MedicoDTO> getMedicoById(@PathVariable Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		return medicoRepository.findById(id)
				.map(medico -> {
					boolean isAdmin = authentication.getAuthorities().stream()
							.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
					if (!isAdmin && (medico.getUsuario() == null
							|| !medico.getUsuario().getEmail().equals(currentPrincipalName))) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).<MedicoDTO>build();
					}
					return ResponseEntity.ok(new MedicoDTO(medico));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/email/{email}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
	public ResponseEntity<MedicoDTO> getMedicoPorEmail(@PathVariable String email) {
		Medico medico = medicoRepository.findAll().stream()
				.filter(m -> m.getUsuario() != null && m.getUsuario().getEmail().equalsIgnoreCase(email))
				.findFirst()
				.orElse(null);

		if (medico == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(new MedicoDTO(medico));
	}



	@PutMapping("/update/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
	public ResponseEntity<MedicoDTO> updateMedico(@PathVariable Integer id,
			@Valid @RequestBody MedicoUpdateProfileDTO medicoAtualizadoDTO) { // DTO changed
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		return medicoRepository.findById(id)
				.map(medico -> {
					boolean isAdmin = authentication.getAuthorities().stream()
							.anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));
					if (!isAdmin && (medico.getUsuario() == null
							|| !medico.getUsuario().getEmail().equals(currentPrincipalName))) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).<MedicoDTO>build();
					}

					Usuario usuario = medico.getUsuario();
					
					if (medicoAtualizadoDTO.getEmail() != null
							&& !medicoAtualizadoDTO.getEmail().equalsIgnoreCase(usuario.getEmail())) {
						if (usuarioRepository.existsByEmail(medicoAtualizadoDTO.getEmail())) {
							throw new RuntimeException("Email já em uso.");
						}
						usuario.setEmail(medicoAtualizadoDTO.getEmail());
					}
					
					if (medicoAtualizadoDTO.getNome() != null) {
						medico.setNome(medicoAtualizadoDTO.getNome());
					}
					if (medicoAtualizadoDTO.getEspecialidade() != null) {
						medico.setEspecialidade(medicoAtualizadoDTO.getEspecialidade());
					}
					if (medicoAtualizadoDTO.getSexo() != null) {
						medico.setSexo(medicoAtualizadoDTO.getSexo());
					}
					if (medicoAtualizadoDTO.getDataNascimento() != null) {
						medico.setDataNascimento(medicoAtualizadoDTO.getDataNascimento());
					}
					if (medicoAtualizadoDTO.getTelefoneConsultorio() != null) {
						medico.setTelefoneConsultorio(medicoAtualizadoDTO.getTelefoneConsultorio());
					}
					if (medicoAtualizadoDTO.getTempoConsulta() != null) {
						medico.setTempoConsulta(medicoAtualizadoDTO.getTempoConsulta());
					}
					if (medicoAtualizadoDTO.getUf() != null) {
						medico.setUf(medicoAtualizadoDTO.getUf());
					}
					if (medicoAtualizadoDTO.getValorConsulta() != null) {
						medico.setValorConsulta(medicoAtualizadoDTO.getValorConsulta());
					}
					
					if (medicoAtualizadoDTO.getCpf() != null && !medicoAtualizadoDTO.getCpf().equalsIgnoreCase(medico.getCpf())) {
					     if (medicoRepository.existsByCpf(medicoAtualizadoDTO.getCpf())) {
					         throw new RuntimeException("CPF já em uso.");
					     }
					     medico.setCpf(medicoAtualizadoDTO.getCpf());
					 }
					 if (medicoAtualizadoDTO.getCrm() != null && !medicoAtualizadoDTO.getCrm().equalsIgnoreCase(medico.getCrm())) {
					     if (medicoRepository.existsByCrm(medicoAtualizadoDTO.getCrm())) {
					         throw new RuntimeException("CRM já em uso.");
					     }
					     medico.setCrm(medicoAtualizadoDTO.getCrm());
					 }


					
					if (medico.getDiasAtendimento() != null) {
						medico.getDiasAtendimento().clear();
					}

					
					if (medicoAtualizadoDTO.getDiasAtendimento() != null
							&& !medicoAtualizadoDTO.getDiasAtendimento().isEmpty()) {
						List<DiasAtendimento> novosDias = medicoAtualizadoDTO.getDiasAtendimento().stream().map(dto -> {
							DiasAtendimento dia = new DiasAtendimento();
							dia.setMedico(medico); 
							dia.setDiaSemana(dto.getDiaSemana());
							dia.setHorario(dto.getHorario());
							dia.setCreatedAt(LocalDateTime.now());
							dia.setUpdatedAt(LocalDateTime.now());
							return dia;
						}).collect(Collectors.toList());
						
						medico.getDiasAtendimento().addAll(novosDias);
					}
					

					Medico updated = medicoRepository.save(medico);
					return ResponseEntity.ok(new MedicoDTO(updated));
				})
				.orElse(ResponseEntity.notFound().build());
	}


	@DeleteMapping("/deletar/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> deleteMedico(@PathVariable Integer id) {
		return medicoRepository.findById(id)
				.map(medico -> {
					medicoRepository.delete(medico);
					return ResponseEntity.noContent().build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/redefinir-senha/{medicoId}")
	@PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
	public ResponseEntity<String> redefinirSenha(@PathVariable Integer medicoId,
			@Valid @RequestBody RedefinirSenhaRequest request) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		Medico medico = medicoRepository.findById(medicoId).orElse(null);
		if (medico == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Médico não encontrado.");
		}

		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		if (!isAdmin && !medico.getUsuario().getEmail().equals(currentPrincipalName)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");
		}

		if (!isAdmin) {
			if (request.getSenhaAntiga() == null || request.getSenhaAntiga().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha antiga é obrigatória.");
			}
			boolean senhaValida = passwordEncoder.matches(request.getSenhaAntiga(), medico.getUsuario().getPassword());
			if (!senhaValida) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha antiga incorreta.");
			}
		}

		String novaSenhaCriptografada = passwordEncoder.encode(request.getNovaSenha());
		medico.getUsuario().setSenha(novaSenhaCriptografada);
		medicoRepository.save(medico);

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
}
