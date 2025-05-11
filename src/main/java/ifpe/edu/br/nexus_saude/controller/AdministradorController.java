package ifpe.edu.br.nexus_saude.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import ifpe.edu.br.nexus_saude.model.Administrador;
import ifpe.edu.br.nexus_saude.model.Agendamento;
import ifpe.edu.br.nexus_saude.model.ConsultaHistorico;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.repository.AdministradorRepository;
import ifpe.edu.br.nexus_saude.repository.AgendamentoRepository;
import ifpe.edu.br.nexus_saude.repository.ConsultaHistoricoRepository;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
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
	private final AdministradorRepository repository;
	private final MedicoRepository medicoRepository;
	private final PacienteRepository pacienteRepository;
	private final AgendamentoRepository agendamentoRepository;
	private final ConsultaHistoricoRepository consultaHistoricoRepository;
	private final DiasAtendimentoRepository diasAtendimentoRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/admin")
	public ResponseEntity<AdministradorDTO> postAdmin(@RequestBody Administrador admin) {
		admin.setSenha(passwordEncoder.encode(admin.getSenha()));
		Administrador savedAdmin = repository.save(admin);
		return ResponseEntity.status(HttpStatus.CREATED).body(new AdministradorDTO(savedAdmin));
	}
	@GetMapping("/listar")
	public List<AdministradorDTO> getAdmins(){
		return repository.findAll()
				.stream()
				.map(AdministradorDTO::new)
				.toList();
	}

	//Atualizar um administrador
	@PutMapping("/{id}")
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
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
		return repository.findById(id)
				.map(admin -> {
					repository.delete(admin);
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
	// métodos do tipo PUT / PATCH para que o administrador atualize qualquer entidade
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
	public ResponseEntity<ConsultaHistoricoDTO> atualizarConsultaHistorico(@PathVariable Integer id, @RequestBody ConsultaHistoricoDTO dto) {
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
	public ResponseEntity<DiasAtendimentoDTO> atualizarDiasAtendimento(@PathVariable Integer id, @RequestBody DiasAtendimentoDTO dto) {
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