package ifpe.edu.br.nexus_saude.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.dto.AdministradorDTO;
import ifpe.edu.br.nexus_saude.dto.AgendamentoDTO;
import ifpe.edu.br.nexus_saude.dto.ConsultaHistoricoDTO;
import ifpe.edu.br.nexus_saude.dto.DiasAtendimentoDTO;
import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.dto.PacienteDTO;
import ifpe.edu.br.nexus_saude.dto.RegistroAdminRequestDTO;
import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.model.Agendamento;
import ifpe.edu.br.nexus_saude.model.ConsultaHistorico;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.Papel;
import ifpe.edu.br.nexus_saude.model.Usuario;
import ifpe.edu.br.nexus_saude.repository.AdministradorRepository;
import ifpe.edu.br.nexus_saude.repository.AgendamentoRepository;
import ifpe.edu.br.nexus_saude.repository.ConsultaHistoricoRepository;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.PapelRepository;
import ifpe.edu.br.nexus_saude.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdministradorController {
	private final AdministradorRepository administradorRepository;
	private final UsuarioRepository usuarioRepository;
	private final PapelRepository papelRepository;
	private final PasswordEncoder passwordEncoder;

	private final MedicoRepository medicoRepository;
	private final PacienteRepository pacienteRepository;
	private final AgendamentoRepository agendamentoRepository;
	private final ConsultaHistoricoRepository consultaHistoricoRepository;
	private final DiasAtendimentoRepository diasAtendimentoRepository;

	@PostMapping("/registrar")
	@PreAuthorize("permitAll()")
	public ResponseEntity<?> registrarAdmin(@Valid @RequestBody RegistroAdminRequestDTO requestDTO) {
		if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
			return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
		}

		Usuario usuario = new Usuario(requestDTO.getEmail(), passwordEncoder.encode(requestDTO.getSenha()));
		Papel adminPapel = papelRepository.findByNome("ADMIN")
				.orElseGet(() -> papelRepository.save(new Papel("ADMIN")));
		usuario.setPapeis(Set.of(adminPapel));

		Administrador admin = new Administrador();
		admin.setUsuario(usuario);

		Administrador savedAdmin = administradorRepository.save(admin);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new AdministradorDTO(savedAdmin.getId(), savedAdmin.getUsuario().getEmail()));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdministradorDTO> updateAdmin(@PathVariable Integer id,
			@RequestBody RegistroAdminRequestDTO adminDetailsDTO) {
		return administradorRepository.findById(id)

				.map(admin -> {
					Usuario usuario = admin.getUsuario();
					if (adminDetailsDTO.getEmail() != null
							&& !adminDetailsDTO.getEmail().equalsIgnoreCase(usuario.getEmail())) {
						if (usuarioRepository.existsByEmail(adminDetailsDTO.getEmail())) {
							throw new RuntimeException("Email já em uso por outro usuário.");
						}
						usuario.setEmail(adminDetailsDTO.getEmail());
					}
					if (adminDetailsDTO.getSenha() != null && !adminDetailsDTO.getSenha().isEmpty()) {
						usuario.setSenha(passwordEncoder.encode(adminDetailsDTO.getSenha()));
					}
					Administrador updatedAdmin = administradorRepository.save(admin);
					return ResponseEntity
							.ok(new AdministradorDTO(updatedAdmin.getId(), updatedAdmin.getUsuario().getEmail()));
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
		return administradorRepository.findById(id)
				.map(admin -> {
					administradorRepository.delete(admin);
					return ResponseEntity.noContent().<Void>build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/medicos")
	public List<MedicoDTO> listarMedicos() {
		return medicoRepository.findAll().stream().map(MedicoDTO::new).toList();
	}

	@GetMapping("/pacientes")
	public List<PacienteDTO> listarPacientes() {
		return pacienteRepository.findAll().stream().map(PacienteDTO::new).toList();
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> atualizarPaciente(@PathVariable Integer id, @RequestBody PacienteDTO dto) {
		return pacienteRepository.findById(id)
				.map(paciente -> {
					Usuario usuario = paciente.getUsuario();

					if (!usuario.getEmail().equalsIgnoreCase(dto.getEmail())) {
						if (usuarioRepository.existsByEmail(dto.getEmail())) {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já está em uso.");
						}
						usuario.setEmail(dto.getEmail());
					}

					paciente.setNomeCompleto(dto.getNomeCompleto());
					paciente.setCpf(dto.getCpf());
					paciente.setTelefone(dto.getTelefone());
					paciente.setDataNascimento(dto.getDataNascimento());
					paciente.setPlanoSaude(dto.getPlanoSaude());

					usuarioRepository.save(usuario);
					pacienteRepository.save(paciente);

					return ResponseEntity.ok("Paciente atualizado com sucesso.");
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado."));
	}

	@GetMapping("/agendamentos")
	public List<AgendamentoDTO> listarAgendamentos() {
		return agendamentoRepository.findAll().stream().map(AgendamentoDTO::new).toList();
	}

	@GetMapping("/historico-consultas")
	public List<ConsultaHistoricoDTO> listarHistoricoConsultas() {
		return consultaHistoricoRepository.findAll().stream().map(ConsultaHistoricoDTO::new).toList();
	}

	@GetMapping("/dias-atendimento")
	public List<DiasAtendimentoDTO> listarDiasAtendimento() {
		return diasAtendimentoRepository.findAll().stream().map(DiasAtendimentoDTO::new).toList();
	}

	@PreAuthorize("permitAll()")
	@GetMapping("/dashboard-stats")
	public Map<String, Integer> getDashboardStats() {
		Map<String, Integer> stats = new HashMap<>();
		stats.put("doctorsCount", medicoRepository.findAll().size());
		stats.put("patientsCount", pacienteRepository.findAll().size());
		stats.put("agendamentosAtivos", agendamentoRepository.findAll().size());
		stats.put("consultasRealizadas", consultaHistoricoRepository.findAll().size());

		return stats;
	}

	@PutMapping("/redefinir-senha/{adminId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> redefinirSenha(@PathVariable Integer adminId,
			@Valid @RequestBody RedefinirSenhaRequest request) {

		Administrador admin = administradorRepository.findById(adminId).orElse(null);
		if (admin == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador não encontrado.");
		}

		String novaSenhaCriptografada = passwordEncoder.encode(request.getNovaSenha());
		admin.getUsuario().setSenha(novaSenhaCriptografada);
		administradorRepository.save(admin);

		return ResponseEntity.ok("Senha do administrador alterada com sucesso.");
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