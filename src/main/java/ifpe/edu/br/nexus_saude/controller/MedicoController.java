package ifpe.edu.br.nexus_saude.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/medico")
    public ResponseEntity<MedicoDTO> postMedico(@RequestBody Medico medico) {
        medico.setSenha(passwordEncoder.encode(medico.getSenha()));
        Medico savedMedico = repository.save(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoDTO(savedMedico));
    }

    @GetMapping("/medico/listar")
    public List<MedicoDTO> getMedicos() {
        return repository.findAll()
                .stream()
                .map(MedicoDTO::new)
                .toList();
    }
}
