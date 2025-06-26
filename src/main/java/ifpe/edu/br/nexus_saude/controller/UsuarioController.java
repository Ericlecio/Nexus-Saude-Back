package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.model.Usuario;
import ifpe.edu.br.nexus_saude.model.Paciente;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.PacienteRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/logado")
    public ResponseEntity<?> getUsuarioLogado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = (Usuario) authentication.getPrincipal();

        String papel = usuario.getPapeis().stream()
                .findFirst()
                .map(p -> p.getNome())
                .orElse("DESCONHECIDO");

        Map<String, Object> response = new HashMap<>();
        response.put("email", usuario.getEmail());
        response.put("papel", papel);

        if (papel.equalsIgnoreCase("PACIENTE")) {
            Optional<Paciente> paciente = pacienteRepository.findAll().stream()
                    .filter(p -> p.getUsuario().getId().equals(usuario.getId()))
                    .findFirst();

            if (paciente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente não encontrado.");
            }

            response.put("nomeCompleto", paciente.get().getNomeCompleto());
            response.put("id", paciente.get().getPacienteId());

        } else if (papel.equalsIgnoreCase("MEDICO")) {
            Optional<Medico> medico = medicoRepository.findAll().stream()
                    .filter(m -> m.getUsuario().getId().equals(usuario.getId()))
                    .findFirst();

            if (medico.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Médico não encontrado.");
            }

            response.put("nomeCompleto", medico.get().getNome());
            response.put("id", medico.get().getId());

        } else if (papel.equalsIgnoreCase("ADMIN")) {
            response.put("nomeCompleto", "Administrador");
            response.put("id", usuario.getId());

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tipo de usuário não reconhecido.");
        }

        return ResponseEntity.ok(response);
    }
}
