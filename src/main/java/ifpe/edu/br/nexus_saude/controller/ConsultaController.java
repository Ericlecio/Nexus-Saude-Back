package ifpe.edu.br.nexus_saude.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.dto.ConsultaDTO;
import ifpe.edu.br.nexus_saude.model.Consulta;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import ifpe.edu.br.nexus_saude.repository.ConsultaRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.SituacaoAgendamentoRepository;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final SituacaoAgendamentoRepository situacaoRepository;

    public ConsultaController(ConsultaRepository consultaRepository, MedicoRepository medicoRepository,
            PacienteRepository pacienteRepository, SituacaoAgendamentoRepository situacaoRepository) {
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.situacaoRepository = situacaoRepository;
    }

    @PostMapping
    public ResponseEntity<ConsultaDTO> criarConsulta(@RequestBody ConsultaDTO dto) {
        Optional<Paciente> paciente = pacienteRepository.findById(dto.getPacienteId());
        Optional<Medico> medico = medicoRepository.findById(dto.getMedicoId());
        Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(dto.getSituacaoId());

        if (paciente.isEmpty() || medico.isEmpty() || situacao.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Consulta consulta = dto.toEntity(paciente.get(), medico.get(), situacao.get());
        Consulta savedConsulta = consultaRepository.save(consulta);

        return ResponseEntity.ok(new ConsultaDTO(savedConsulta));
    }
}
