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

	    // 🔹 GET: List all ConsultaHistorico
	    @GetMapping
	    public List<ConsultaHistoricoDTO> listarConsultasHistorico() {
	        return consultaHistoricoRepository.findAll()
	                .stream()
	                .map(consulta -> new ConsultaHistoricoDTO(
	                        consulta.getId(),
	                        consulta.getData(),
	                        consulta.getDataAtualizacao(),
	                        consulta.getEspecialidade(),
	                        consulta.getLocal(),
	                        consulta.getMedico().getNome(),
	                        consulta.getPaciente().getNomeCompleto(),
	                        consulta.getSituacao().getDescricao(),
	                        consulta.getTelefoneConsultorio(),
	                        consulta.getValorConsulta()
	                ))
	                .toList();
	    }

	    // 🔹 GET: Get ConsultaHistorico by ID
	    @GetMapping("/{id}")
	    public ResponseEntity<ConsultaHistoricoDTO> obterConsultaHistoricoPorId(@PathVariable Integer id) {
	        return consultaHistoricoRepository.findById(id)
	                .map(consulta -> ResponseEntity.ok(new ConsultaHistoricoDTO(
	                        consulta.getId(),
	                        consulta.getData(),
	                        consulta.getDataAtualizacao(),
	                        consulta.getEspecialidade(),
	                        consulta.getLocal(),
	                        consulta.getMedico().getNome(),
	                        consulta.getPaciente().getNomeCompleto(),
	                        consulta.getSituacao().getDescricao(),
	                        consulta.getTelefoneConsultorio(),
	                        consulta.getValorConsulta()
	                )))
	                .orElseGet(() -> ResponseEntity.notFound().build());
	    }

	    // 🔹 POST: Create a new ConsultaHistorico
	    @PostMapping
	    public ResponseEntity<ConsultaHistoricoDTO> criarConsultaHistorico(@RequestBody ConsultaHistoricoDTO dto) {
	        Optional<Paciente> paciente = pacienteRepository.findById(dto.getId());
	        Optional<Medico> medico = medicoRepository.findById(dto.getId());
	        Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getId());

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
	        return ResponseEntity.ok(new ConsultaHistoricoDTO(
	                savedConsulta.getId(),
	                savedConsulta.getData(),
	                savedConsulta.getDataAtualizacao(),
	                savedConsulta.getEspecialidade(),
	                savedConsulta.getLocal(),
	                savedConsulta.getMedico().getNome(),
	                savedConsulta.getPaciente().getNomeCompleto(),
	                savedConsulta.getSituacao().getDescricao(),
	                savedConsulta.getTelefoneConsultorio(),
	                savedConsulta.getValorConsulta()
	        ));
	    }

	    // 🔹 PUT: Update an existing ConsultaHistorico
	    @PutMapping("/{id}")
	    public ResponseEntity<ConsultaHistoricoDTO> atualizarConsultaHistorico(@PathVariable Integer id, @RequestBody ConsultaHistoricoDTO dto) {
	        Optional<ConsultaHistorico> optionalConsulta = consultaHistoricoRepository.findById(id);

	        if (optionalConsulta.isEmpty()) {
	            return ResponseEntity.notFound().build(); // 🔹 Handle 404 outside `.map()`
	        }

	        ConsultaHistorico consulta = optionalConsulta.get();
	        Optional<Paciente> paciente = pacienteRepository.findById(dto.getId());
	        Optional<Medico> medico = medicoRepository.findById(dto.getId());
	        Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getId());

	        if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
	            return ResponseEntity.badRequest().build(); // 🔹 Handle bad request outside `.map()`
	        }

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
	        
	        ConsultaHistoricoDTO responseDTO = new ConsultaHistoricoDTO(
	                updatedConsulta.getId(),
	                updatedConsulta.getData(),
	                updatedConsulta.getDataAtualizacao(),
	                updatedConsulta.getEspecialidade(),
	                updatedConsulta.getLocal(),
	                updatedConsulta.getMedico().getNome(),
	                updatedConsulta.getPaciente().getNomeCompleto(),
	                updatedConsulta.getSituacao().getDescricao(),
	                updatedConsulta.getTelefoneConsultorio(),
	                updatedConsulta.getValorConsulta()
	        );

	        return ResponseEntity.ok(responseDTO); // ✅ Proper return type!
	    }

	    // 🔹 DELETE: Remove a ConsultaHistorico
	    @DeleteMapping("/{id}")
	    public ResponseEntity<?> deletarConsultaHistorico(@PathVariable Integer id) {
	        return consultaHistoricoRepository.findById(id)
	                .map(consulta -> {
	                    consultaHistoricoRepository.delete(consulta);
	                    return ResponseEntity.ok("Consulta histórica deletada com sucesso!");
	                })
	                .orElseGet(() -> ResponseEntity.status(404).body("Consulta histórica não encontrada"));
	    }

}
