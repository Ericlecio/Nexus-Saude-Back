package ifpe.edu.br.nexus_saude.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.dto.AgendamentoDTO;
import ifpe.edu.br.nexus_saude.model.Agendamento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import ifpe.edu.br.nexus_saude.repository.AgendamentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.SituacaoAgendamentoRepository;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {
	private final AgendamentoRepository agendamentoRepository;
	private final MedicoRepository medicoRepository;
	private final PacienteRepository pacienteRepository;
	private final SituacaoAgendamentoRepository situacaoRepository;

	public AgendamentoController(AgendamentoRepository agendamentoRepository, MedicoRepository medicoRepository,
			PacienteRepository pacienteRepository, SituacaoAgendamentoRepository situacaoRepository) {
		this.agendamentoRepository = agendamentoRepository;
		this.medicoRepository = medicoRepository;
		this.pacienteRepository = pacienteRepository;
		this.situacaoRepository = situacaoRepository;
	}

	// 🔹 GET: List all Agendamentos
	@GetMapping
	public List<AgendamentoDTO> listarAgendamentos() {
		return agendamentoRepository.findAll()
				.stream()
				.map(AgendamentoDTO::new)
				.toList();
	}

	// 🔹 GET: Get Agendamento by ID
	@GetMapping("/{id}")
	public ResponseEntity<AgendamentoDTO> obterAgendamentoPorId(@PathVariable Integer id) {
		return agendamentoRepository.findById(id)
				.map(agendamento -> ResponseEntity.ok(new AgendamentoDTO(agendamento)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 POST: Create a new Agendamento
	@PostMapping
	public ResponseEntity<AgendamentoDTO> criarAgendamento(@RequestBody AgendamentoDTO dto) {
		Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
		Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
		Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getSituacaoId());

		if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		Agendamento agendamento = Agendamento.builder()
				.data(dto.getData())
				.especialidade(dto.getEspecialidade())
				.local(dto.getLocal())
				.medico(medico.get())
				.paciente(paciente.get())
				.situacao(situacao.get())
				.telefoneConsultorio(dto.getTelefoneConsultorio())
				.valorConsulta(dto.getValorConsulta())
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now())
				.build();

		Agendamento savedAgendamento = agendamentoRepository.save(agendamento);
		return ResponseEntity.ok(new AgendamentoDTO(savedAgendamento));
	}

	// 🔹 PUT: Update an existing Agendamento
	@PutMapping("/{id}")
	public ResponseEntity<AgendamentoDTO> atualizarAgendamento(@PathVariable Integer id, @RequestBody AgendamentoDTO dto) {
	    Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);

	    if (optionalAgendamento.isEmpty()) {
	        return ResponseEntity.notFound().build(); // 🔹 Handle 404 outside `.map()`
	    }

	    Agendamento agendamento = optionalAgendamento.get();
	    Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
	    Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
	    Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getSituacaoId());

	    if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
	        return ResponseEntity.badRequest().build(); // 🔹 Handle bad request outside `.map()`
	    }

	    agendamento.setData(dto.getData());
	    agendamento.setEspecialidade(dto.getEspecialidade());
	    agendamento.setLocal(dto.getLocal());
	    agendamento.setMedico(medico.get());
	    agendamento.setPaciente(paciente.get());
	    agendamento.setSituacao(situacao.get());
	    agendamento.setTelefoneConsultorio(dto.getTelefoneConsultorio());
	    agendamento.setValorConsulta(dto.getValorConsulta());
	    agendamento.setUpdatedAt(LocalDateTime.now());

	    Agendamento updatedAgendamento = agendamentoRepository.save(agendamento);
	    return ResponseEntity.ok(new AgendamentoDTO(updatedAgendamento)); // ✅ Proper return type!
	}

	// 🔹 DELETE: Remove an Agendamento
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarAgendamento(@PathVariable Integer id) {
		return agendamentoRepository.findById(id)
				.map(agendamento -> {
					agendamentoRepository.delete(agendamento);
					return ResponseEntity.ok("Agendamento deletado com sucesso!");
				})
				.orElseGet(() -> ResponseEntity.status(404).body("Agendamento não encontrado"));
	}

}
