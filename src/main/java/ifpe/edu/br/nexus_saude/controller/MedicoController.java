package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private DiasAtendimentoRepository diasAtendimentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Listar todos os médicos (retornando lista de DTOs)
    @GetMapping("/listar")
    public List<MedicoDTO> listar() {
        return medicoRepository.findAll()
                .stream()
                .map(MedicoDTO::new)
                .toList();
    }

    // Buscar médico por id (retorna DTO)
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> obterPorId(@PathVariable Integer id) {
        return medicoRepository.findById(id)
                .map(medico -> ResponseEntity.ok(new MedicoDTO(medico)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Inserir novo médico
    @PostMapping("/inserir")
    public ResponseEntity<MedicoDTO> inserir(@RequestBody Medico medico) {
        // Normaliza dados e criptografa senha
        medico.setEmail(medico.getEmail().toLowerCase());
        medico.setSenha(passwordEncoder.encode(medico.getSenha()));

        // Associa os dias de atendimento ao médico
        if (medico.getDiasAtendimento() != null && !medico.getDiasAtendimento().isEmpty()) {
            for (DiasAtendimento dia : medico.getDiasAtendimento()) {
                dia.setMedico(medico);
            }
        }

        Medico salvo = medicoRepository.save(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MedicoDTO(salvo));
    }

    // Atualizar médico
    @PutMapping("/update/{id}")
    public ResponseEntity<MedicoDTO> atualizar(@PathVariable Integer id, @RequestBody Medico medicoAtualizado) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    medico.setNome(medicoAtualizado.getNome());
                    medico.setEmail(medicoAtualizado.getEmail().toLowerCase());
                    if (medicoAtualizado.getSenha() != null && !medicoAtualizado.getSenha().isEmpty()) {
                        medico.setSenha(passwordEncoder.encode(medicoAtualizado.getSenha()));
                    }
                    medico.setCrm(medicoAtualizado.getCrm());
                    medico.setEspecialidade(medicoAtualizado.getEspecialidade());

                    if (medicoAtualizado.getDiasAtendimento() != null) {
                        medico.getDiasAtendimento().clear();
                        for (DiasAtendimento dia : medicoAtualizado.getDiasAtendimento()) {
                            dia.setMedico(medico);
                            medico.getDiasAtendimento().add(dia);
                        }
                    }

                    Medico atualizado = medicoRepository.save(medico);
                    return ResponseEntity.ok(new MedicoDTO(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        Optional<Medico> medico = medicoRepository.findById(id);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        try {
            medicoRepository.delete(medico.get());
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi possível deletar o médico. Verifique se há registros vinculados.");
        }
    }

}
