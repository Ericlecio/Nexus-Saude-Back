package ifpe.edu.br.nexus_saude.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

	// Injeção de outras dependências (MedicoRepository, etc.)
	private final MedicoRepository medicoRepository;
	private final PacienteRepository pacienteRepository;
	private final AgendamentoRepository agendamentoRepository;
	private final ConsultaHistoricoRepository consultaHistoricoRepository;
	private final DiasAtendimentoRepository diasAtendimentoRepository;

	@PostMapping("/registrar") // Novo endpoint para registrar admin (pode ser público inicialmente ou protegido)
	@PreAuthorize("permitAll()") // Exemplo: permitir que qualquer um registre o primeiro admin, depois proteger
	public ResponseEntity<?> registrarAdmin(@Valid @RequestBody RegistroAdminRequestDTO requestDTO) {
		if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
			return ResponseEntity.badRequest().body("Erro: Email já está em uso!");
		}

		Usuario usuario = new Usuario(requestDTO.getEmail(), passwordEncoder.encode(requestDTO.getSenha()));
		Papel adminPapel = papelRepository.findByNome("ADMIN")
				.orElseGet(() -> papelRepository.save(new Papel("ADMIN"))); // Cria se não existir
		usuario.setPapeis(Set.of(adminPapel));
		// Não precisa salvar o usuário aqui se o cascade estiver configurado em Administrador.usuario

		Administrador admin = new Administrador();
		admin.setUsuario(usuario);
		// admin.setNomeCompleto(requestDTO.getNomeCompleto()); // Se tiver este campo no DTO

		Administrador savedAdmin = administradorRepository.save(admin);
		// Use um DTO de resposta para não expor a senha ou detalhes excessivos
		return ResponseEntity.status(HttpStatus.CREATED).body(new AdministradorDTO(savedAdmin.getId(), savedAdmin.getUsuario().getEmail()));
	}

	// Endpoint antigo de postAdmin precisa ser revisto ou removido se /registrar cobrir o caso
	// @PostMapping("/admin") // Este endpoint parece redundante com /registrar
	// @PreAuthorize("hasRole('ADMIN')")
	// public ResponseEntity<AdministradorDTO> postAdmin(@RequestBody Administrador admin) {
	//     // A lógica aqui precisa ser adaptada para usar a entidade Usuario
	//     // e provavelmente deveria usar um DTO de request também.
	//     // admin.getUsuario().setSenha(passwordEncoder.encode(admin.getUsuario().getSenha()));
	//     // Administrador savedAdmin = repository.save(admin);
	//     // return ResponseEntity.status(HttpStatus.CREATED).body(new AdministradorDTO(savedAdmin));
	//     return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build(); // Exemplo
	// }

	//Atualizar um administrador
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<AdministradorDTO> updateAdmin(@PathVariable Integer id, @RequestBody RegistroAdminRequestDTO adminDetailsDTO) {
		return administradorRepository.findById(id)

				.map(admin -> {
					Usuario usuario = admin.getUsuario();
					// Atualizar email apenas se fornecido e diferente, e não existente
					if (adminDetailsDTO.getEmail() != null && !adminDetailsDTO.getEmail().equalsIgnoreCase(usuario.getEmail())) {
						if (usuarioRepository.existsByEmail(adminDetailsDTO.getEmail())) {
							throw new RuntimeException("Email já em uso por outro usuário."); // Ou retorne BadRequest
						}
						usuario.setEmail(adminDetailsDTO.getEmail());
					}
					if (adminDetailsDTO.getSenha() != null && !adminDetailsDTO.getSenha().isEmpty()) {
						usuario.setSenha(passwordEncoder.encode(adminDetailsDTO.getSenha()));
					}
					// admin.setNomeCompleto(adminDetailsDTO.getNomeCompleto()); // Se tiver
					// usuarioRepository.save(usuario); // Salvo em cascata por admin
					Administrador updatedAdmin = administradorRepository.save(admin);
					return ResponseEntity.ok(new AdministradorDTO(updatedAdmin.getId(), updatedAdmin.getUsuario().getEmail()));
				})
				.orElse(ResponseEntity.notFound().build());
	}


	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
		return administradorRepository.findById(id)
				.map(admin -> {
					administradorRepository.delete(admin); // Cascade deve remover o Usuario associado se orphanRemoval=true
					return ResponseEntity.noContent().<Void>build();
				})
				.orElse(ResponseEntity.notFound().build());
	}

	// métodos GET, listam todos as entidades no sistema
	@GetMapping("/medicos")
	public List<MedicoDTO> listarMedicos() {
		return medicoRepository.findAll().stream().map(MedicoDTO::new).toList();
	}

	@GetMapping("/pacientes")
	public List<PacienteDTO> listarPacientes() {
		return pacienteRepository.findAll().stream().map(PacienteDTO::new).toList();
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

	@GetMapping("/dashboard-stats")
	public Map<String, Integer> getDashboardStats() {
		Map<String, Integer> stats = new HashMap<>();
		stats.put("doctorsCount", medicoRepository.findAll().size());
		stats.put("patientsCount", pacienteRepository.findAll().size());
		stats.put("agendamentosAtivos", agendamentoRepository.findAll().size());
		stats.put("consultasRealizadas", consultaHistoricoRepository.findAll().size());

		return stats;
	}

	// métodos do tipo PUT / PATCH para que o administrador atualize qualquer
	// entidade
	// 🔹 Atualizar um Médico
	@PutMapping("/medico/{id}")
	public ResponseEntity<MedicoDTO> atualizarMedico(@PathVariable Integer id, @RequestBody MedicoDTO dto) {
		return medicoRepository.findById(id)
				.map(medico -> {
					medico.setNome(dto.getNome());
					medico.setEspecialidade(dto.getEspecialidade());
					Medico updatedMedico = medicoRepository.save(medico);
					return ResponseEntity.ok(new MedicoDTO(updatedMedico));
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 Atualizar um Paciente
	@PutMapping("/paciente/{id}")
	public ResponseEntity<PacienteDTO> atualizarPaciente(@PathVariable Integer id, @RequestBody PacienteDTO dto) {
		return pacienteRepository.findById(id)
				.map(paciente -> {
					paciente.setNomeCompleto(dto.getNomeCompleto());
					paciente.setDataNascimento(dto.getDataNascimento());
					Paciente updatedPaciente = pacienteRepository.save(paciente);
					return ResponseEntity.ok(new PacienteDTO(updatedPaciente));
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 Atualizar um Agendamento

	// 🔹 Atualizar um Histórico de Consulta
	@PutMapping("/consulta-historico/{id}")
	public ResponseEntity<ConsultaHistoricoDTO> atualizarConsultaHistorico(@PathVariable Integer id,
			@RequestBody ConsultaHistoricoDTO dto) {
		return consultaHistoricoRepository.findById(id)
				.map(consulta -> {
					consulta.setEspecialidade(dto.getEspecialidade());
					consulta.setLocal(dto.getLocal());
					consulta.setDataAtualizacao(LocalDateTime.now());
					ConsultaHistorico updatedConsulta = consultaHistoricoRepository.save(consulta);
					return ResponseEntity.ok(new ConsultaHistoricoDTO(updatedConsulta));
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 Atualizar Dias de Atendimento
	@PutMapping("/dias-atendimento/{id}")
	public ResponseEntity<DiasAtendimentoDTO> atualizarDiasAtendimento(@PathVariable Integer id,
			@RequestBody DiasAtendimentoDTO dto) {
		return diasAtendimentoRepository.findById(id)
				.map(dia -> {
					dia.setDiaSemana(dto.getDiaSemana());
					dia.setHorario(dto.getHorario());
					dia.setUpdatedAt(LocalDateTime.now());
					DiasAtendimento updatedDia = diasAtendimentoRepository.save(dia);
					return ResponseEntity.ok(new DiasAtendimentoDTO(updatedDia));
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// métodos do tipo DELETE
	@DeleteMapping("/medico/{id}")
	public ResponseEntity<?> deletarMedico(@PathVariable Integer id) {
		return medicoRepository.findById(id)
				.map(medico -> {
					medicoRepository.delete(medico);
					return ResponseEntity.ok("Médico removido pelo administrador!");
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/paciente/{id}")
	public ResponseEntity<?> deletarPaciente(@PathVariable Integer id) {

		return pacienteRepository.findById(id)
				.map(paciente -> {
					pacienteRepository.delete(paciente);
					return ResponseEntity.ok("Paciente removido pelo administrador!");
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/agendamento/{id}")
	public ResponseEntity<?> deletarAgendamento(@PathVariable Integer id) {
		return agendamentoRepository.findById(id)
				.map(agendamento -> {
					agendamentoRepository.delete(agendamento);
					return ResponseEntity.ok("Agendamento removido pelo administrador!");
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/consulta-historico/{id}")
	public ResponseEntity<?> deletarConsultaHistorico(@PathVariable Integer id) {
		return consultaHistoricoRepository.findById(id)
				.map(consulta -> {
					consultaHistoricoRepository.delete(consulta);
					return ResponseEntity.ok("Consulta histórica removida pelo administrador!");
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/dias-atendimento/{id}")
	public ResponseEntity<?> deletarDiasAtendimento(@PathVariable Integer id) {
		return diasAtendimentoRepository.findById(id)
				.map(dia -> {
					diasAtendimentoRepository.delete(dia);
					return ResponseEntity.ok("Dias de atendimento removidos pelo administrador!");
				})
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}