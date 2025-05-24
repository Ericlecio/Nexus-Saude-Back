package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.PacienteDTO;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/paciente")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Método para listar todos os pacientes
    @GetMapping("/listar")
    public List<PacienteDTO> getPacientes() {
        return repository.findAll()
                .stream()
                .map(PacienteDTO::new)
                .toList();
    }

    // Método para obter os dados de um paciente específico por ID
    @GetMapping("/{pacienteId}")
    public ResponseEntity<PacienteDTO> getPaciente(@PathVariable Integer pacienteId) {
        Optional<Paciente> paciente = repository.findById(pacienteId);
        return paciente.map(p -> ResponseEntity.ok(new PacienteDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Método para atualizar os dados do paciente
    @PutMapping("/update/{pacienteId}")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Integer pacienteId,
            @RequestBody Paciente pacienteAtualizado) {
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

    // Método para deletar um paciente
    @DeleteMapping("/deletar/{pacienteId}")
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

        if (paciente.getSenha() != null) {
            paciente.setSenha(passwordEncoder.encode(paciente.getSenha()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

        Paciente savedPaciente = repository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PacienteDTO(savedPaciente));
    }

    // Método para redefinir a senha do paciente
    @PutMapping("/redefinir-senha/{pacienteId}")
    public ResponseEntity<String> redefinirSenha(@PathVariable Integer pacienteId,
            @RequestBody RedefinirSenhaRequest request) {
        Paciente paciente = repository.findById(pacienteId).orElse(null);
        if (paciente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado.");
        }

        boolean senhaValida = passwordEncoder.matches(request.getSenhaAntiga(), paciente.getSenha());
        if (!senhaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha antiga incorreta.");
        }

        String novaSenhaCriptografada = passwordEncoder.encode(request.getNovaSenha());
        paciente.setSenha(novaSenhaCriptografada);
        repository.save(paciente);

        return ResponseEntity.ok("Senha alterada com sucesso.");
    }

    // Classe para receber a senha antiga e nova no request
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
