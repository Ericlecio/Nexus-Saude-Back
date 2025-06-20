package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.AgendamentoDTO;
import ifpe.edu.br.nexus_saude.model.Agendamento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import ifpe.edu.br.nexus_saude.repository.AgendamentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.SituacaoAgendamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/agendamento")
public class AgendamentoController {

	@Autowired
	private AgendamentoRepository agendamentoRepository;

	@Autowired
	private MedicoRepository medicoRepository;

	@Autowired
	private PacienteRepository pacienteRepository;

	@Autowired
	private SituacaoAgendamentoRepository situacaoRepository;

	@GetMapping("/listar")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public List<AgendamentoDTO> listar() {
		return agendamentoRepository.findAll()
				.stream()
				.map(AgendamentoDTO::new)
				.toList();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<AgendamentoDTO> obterPorId(@PathVariable Integer id) {
		Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
		return agendamento.map(a -> ResponseEntity.ok(new AgendamentoDTO(a)))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/medico/{medicoId}")
	@PreAuthorize("hasAnyRole('ADMIN','MEDICO')")
	public List<AgendamentoDTO> listarPorMedico(@PathVariable Integer medicoId) {
		return agendamentoRepository.findByMedicoIdAndDataAfter(medicoId, LocalDateTime.now())
				.stream()
				.map(AgendamentoDTO::new)
				.toList();
	}

	@PostMapping("/inserir")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<?> inserir(@RequestBody AgendamentoDTO dto) {
		Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
		Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
		Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(1);

		if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
			return ResponseEntity.badRequest().body("Paciente, Médico ou Situação inválidos.");
		}

		boolean existeConflito = agendamentoRepository.existsByMedicoIdAndData(medico.get().getId(), dto.getData());
		if (existeConflito) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Já existe um agendamento para esse médico no horário escolhido.");
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

		Agendamento saved = agendamentoRepository.save(agendamento);
		return ResponseEntity.status(HttpStatus.CREATED).body(new AgendamentoDTO(saved));
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<?> atualizar(@PathVariable Integer id, @RequestBody AgendamentoDTO dto) {
		Optional<Agendamento> optAgendamento = agendamentoRepository.findById(id);
		if (optAgendamento.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
		Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
		Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getSituacaoId());

		if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
			return ResponseEntity.badRequest().body("Paciente, Médico ou Situação inválidos.");
		}

		boolean existeConflito = agendamentoRepository.existsByMedicoIdAndDataAndAgendamentoIdNot(
				medico.get().getId(), dto.getData(), id);

		if (existeConflito) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Já existe um agendamento para esse médico no horário escolhido.");
		}

		Agendamento agendamento = optAgendamento.get();
		agendamento.setData(dto.getData());
		agendamento.setEspecialidade(dto.getEspecialidade());
		agendamento.setLocal(dto.getLocal());
		agendamento.setMedico(medico.get());
		agendamento.setPaciente(paciente.get());
		agendamento.setSituacao(situacao.get());
		agendamento.setTelefoneConsultorio(dto.getTelefoneConsultorio());
		agendamento.setValorConsulta(dto.getValorConsulta());
		agendamento.setUpdatedAt(LocalDateTime.now());

		Agendamento updated = agendamentoRepository.save(agendamento);
		return ResponseEntity.ok(new AgendamentoDTO(updated));
	}

	@DeleteMapping("/deletar/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<?> deletar(@PathVariable Integer id) {
		Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
		if (agendamento.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		agendamentoRepository.delete(agendamento.get());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/listarPorMedico")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<List<AgendamentoDTO>> listarPorMedico(
			@RequestParam Integer medicoId,
			@RequestParam(required = false) String situacao) {

		List<Agendamento> agendamentos = agendamentoRepository.findByMedicoId(medicoId);

		if (situacao != null && !situacao.trim().isEmpty()) {
			String filtroLower = situacao.trim().toLowerCase();
			agendamentos = agendamentos.stream()
					.filter(a -> a.getSituacao().getDescricao() != null &&
							a.getSituacao().getDescricao().trim().toLowerCase().equals(filtroLower))
					.collect(Collectors.toList());
		}

		List<AgendamentoDTO> dtos = agendamentos.stream()
				.map(AgendamentoDTO::new)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}

}
