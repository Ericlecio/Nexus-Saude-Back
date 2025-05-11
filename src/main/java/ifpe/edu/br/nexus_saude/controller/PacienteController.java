package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.PacienteDTO;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;
    @PostMapping("/inserir")
    public ResponseEntity<PacienteDTO> postPaciente(@RequestBody Paciente paciente) {
        paciente.setEmail(paciente.getEmail().toLowerCase());
        Paciente savedPaciente = repository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteDTO(savedPaciente));
    }

    @GetMapping("/listar")
    public List<PacienteDTO> getPacientes() {
        return repository.findAll()
                .stream()
                .map(PacienteDTO::new)
                .toList();
    }
    @PutMapping("/{pacienteId}")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Integer pacienteId, @RequestBody Paciente pacienteAtualizado) {

        return repository.findById(pacienteId)
                .map(paciente -> {
                    paciente.setNomeCompleto(pacienteAtualizado.getNomeCompleto());
                    paciente.setEmail(pacienteAtualizado.getEmail());
                    paciente.setTelefone(pacienteAtualizado.getTelefone());
                    paciente.setCpf(pacienteAtualizado.getCpf());
                    paciente.setDataNascimento(pacienteAtualizado.getDataNascimento());
                    paciente.setPlanoSaude(pacienteAtualizado.getPlanoSaude());
                    Paciente updated = repository.save(paciente);
                    return ResponseEntity.ok(new PacienteDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{pacienteId}")
    public ResponseEntity<Object> deletePaciente(@PathVariable Integer pacienteId) {
        return repository.findById(pacienteId)
                .map(paciente -> {
                    repository.delete(paciente);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Método para inserir um novo paciente
    @PostMapping("/inserir")
    public ResponseEntity<PacienteDTO> postPaciente(@RequestBody Paciente paciente) {
        paciente.setEmail(paciente.getEmail().toLowerCase());
        Paciente savedPaciente = repository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteDTO(savedPaciente));
    }
}
