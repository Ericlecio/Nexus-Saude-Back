package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.MedicoDTO;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // Método para cadastrar o médico e os dias de atendimento
    @PostMapping("/inserir")
    public ResponseEntity<Medico> postMedico(@RequestBody Medico medico) {
        // Criação do médico
        medico.setSenha(passwordEncoder.encode(medico.getSenha()));
        medico.setEmail(medico.getEmail().toLowerCase());
        Medico savedMedico = medicoRepository.save(medico);

        // Associando e salvando os dias de atendimento
        if (medico.getDiasAtendimento() != null && !medico.getDiasAtendimento().isEmpty()) {
            for (DiasAtendimento diaAtendimento : medico.getDiasAtendimento()) {
                diaAtendimento.setMedico(savedMedico); 
                diasAtendimentoRepository.save(diaAtendimento); 
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedico);
    }

    // Método para listar todos os médicos
    @GetMapping("/listar")
    public List<Medico> getMedicos() {
        return medicoRepository.findAll();
    }

    // Método para buscar o médico pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> getMedicoById(@PathVariable Integer id) {
        return medicoRepository.findById(id)
                .map(medico -> ResponseEntity.ok(new MedicoDTO(medico))) 
                .orElse(ResponseEntity.notFound().build()); 
    }

    // Método para atualizar o médico
    @PutMapping("/update/{id}")
    public ResponseEntity<Medico> updateMedico(@PathVariable Integer id, @RequestBody Medico medicoAtualizado) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    medico.setNome(medicoAtualizado.getNome());
                    medico.setEmail(medicoAtualizado.getEmail());
                    medico.setSenha(passwordEncoder.encode(medicoAtualizado.getSenha()));
                    medico.setCrm(medicoAtualizado.getCrm());
                    medico.setEspecialidade(medicoAtualizado.getEspecialidade());
                    Medico updated = medicoRepository.save(medico);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Método para deletar o médico
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> deleteMedico(@PathVariable Integer id) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    medicoRepository.delete(medico);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
