package ifpe.edu.br.nexus_saude.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ifpe.edu.br.nexus_saude.dto.ConsultaHistoricoDTO;
import ifpe.edu.br.nexus_saude.model.ConsultaHistorico;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import ifpe.edu.br.nexus_saude.repository.ConsultaHistoricoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.SituacaoAgendamentoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/consulta-historico")
@CrossOrigin(origins = "http://localhost:5173")
public class ConsultaHistoricoController {
	private final ConsultaHistoricoRepository consultaHistoricoRepository;
	private final MedicoRepository medicoRepository;
	private final PacienteRepository pacienteRepository;
	private final SituacaoAgendamentoRepository situacaoRepository;

	public ConsultaHistoricoController(ConsultaHistoricoRepository consultaHistoricoRepository,
			MedicoRepository medicoRepository,
			PacienteRepository pacienteRepository,
			SituacaoAgendamentoRepository situacaoRepository) {
		this.consultaHistoricoRepository = consultaHistoricoRepository;
		this.medicoRepository = medicoRepository;
		this.pacienteRepository = pacienteRepository;
		this.situacaoRepository = situacaoRepository;
		
	}
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public List<ConsultaHistoricoDTO> listarConsultasHistorico() {
	    return consultaHistoricoRepository.findAll()
	            .stream()
	            // MODO CORRIGIDO:
	            .map(ConsultaHistoricoDTO::new)
	            .toList();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<ConsultaHistoricoDTO> obterConsultaHistoricoPorId(@PathVariable Integer id) {
	    return consultaHistoricoRepository.findById(id)
	            // MODO CORRIGIDO:
	            .map(consulta -> ResponseEntity.ok(new ConsultaHistoricoDTO(consulta)))
	            .orElseGet(() -> ResponseEntity.notFound().build());
	}
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<ConsultaHistoricoDTO> criarConsultaHistorico(@RequestBody ConsultaHistoricoDTO dto) {
	    // CORREÇÃO: Use os IDs específicos do DTO
	    Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
	    Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
	    Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getSituacaoId());

	    if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
	        return ResponseEntity.badRequest().build();
	    }

	    ConsultaHistorico consultaHistorico = ConsultaHistorico.builder()
	            .data(dto.getData())
	            .dataAtualizacao(LocalDateTime.now())
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

	    ConsultaHistorico savedConsulta = consultaHistoricoRepository.save(consultaHistorico);

	    // Crie o DTO de resposta a partir da entidade salva
	    ConsultaHistoricoDTO responseDTO = new ConsultaHistoricoDTO(savedConsulta);

	    return ResponseEntity.ok(responseDTO);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<ConsultaHistoricoDTO> atualizarConsultaHistorico(@PathVariable Integer id,
	        @RequestBody ConsultaHistoricoDTO dto) {
	    Optional<ConsultaHistorico> optionalConsulta = consultaHistoricoRepository.findById(id);

	    if (optionalConsulta.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    // CORREÇÃO 1: Usar os IDs corretos do DTO
	    Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
	    Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
	    Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getSituacaoId());

	    if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
	        return ResponseEntity.badRequest().build();
	    }

	    ConsultaHistorico consulta = optionalConsulta.get();
	    consulta.setData(dto.getData());
	    consulta.setDataAtualizacao(LocalDateTime.now());
	    consulta.setEspecialidade(dto.getEspecialidade());
	    consulta.setLocal(dto.getLocal());
	    consulta.setMedico(medico.get());
	    consulta.setPaciente(paciente.get());
	    consulta.setSituacao(situacao.get());
	    consulta.setTelefoneConsultorio(dto.getTelefoneConsultorio());
	    consulta.setValorConsulta(dto.getValorConsulta());
	    consulta.setUpdatedAt(LocalDateTime.now());

	    ConsultaHistorico updatedConsulta = consultaHistoricoRepository.save(consulta);

	    // CORREÇÃO 2: Usar o construtor da entidade para criar o DTO de resposta
	    ConsultaHistoricoDTO responseDTO = new ConsultaHistoricoDTO(updatedConsulta);

	    return ResponseEntity.ok(responseDTO);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
	public ResponseEntity<?> deletarConsultaHistorico(@PathVariable Integer id) {
		return consultaHistoricoRepository.findById(id)
				.map(consulta -> {
					consultaHistoricoRepository.delete(consulta);
					return ResponseEntity.ok("Consulta histórica deletada com sucesso!");
				})
				.orElseGet(() -> ResponseEntity.status(404).body("Consulta histórica não encontrada"));
	}
}