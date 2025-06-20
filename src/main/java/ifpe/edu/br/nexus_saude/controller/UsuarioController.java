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

        String nomeCompleto = null;
        Integer idEspecifico = null;

        if (papel.equalsIgnoreCase("PACIENTE")) {
            Optional<Paciente> paciente = pacienteRepository.findAll().stream()
                    .filter(p -> p.getUsuario().getId().equals(usuario.getId()))
                    .findFirst();

            if (paciente.isPresent()) {
                nomeCompleto = paciente.get().getNomeCompleto();
                idEspecifico = paciente.get().getPacienteId();
            }

        } else if (papel.equalsIgnoreCase("MEDICO")) {
            Optional<Medico> medico = medicoRepository.findAll().stream()
                    .filter(m -> m.getUsuario().getId().equals(usuario.getId()))
                    .findFirst();

            if (medico.isPresent()) {
                nomeCompleto = medico.get().getNome();
                idEspecifico = medico.get().getId();
            }

        } else if (papel.equalsIgnoreCase("ADMIN")) {
            nomeCompleto = "Administrador";
        }

        return ResponseEntity.ok(Map.of(
                "email", usuario.getEmail(),
                "papel", papel,
                "nomeCompleto", nomeCompleto,
                "id", idEspecifico
        ));
    }
}
