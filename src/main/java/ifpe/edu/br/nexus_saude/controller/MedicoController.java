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
@RequestMapping("/Medico")
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
        // Normaliza dados
        medico.setSenha(passwordEncoder.encode(medico.getSenha()));
        medico.setEmail(medico.getEmail().toLowerCase());

        // Associa o médico em cada dia de atendimento, necessário para persistência
        // correta
        if (medico.getDiasAtendimento() != null && !medico.getDiasAtendimento().isEmpty()) {
            for (DiasAtendimento diaAtendimento : medico.getDiasAtendimento()) {
                diaAtendimento.setMedico(medico); // Associação direta antes de salvar
            }
        }

        // Salva tudo de uma vez (inclusive os dias) com CascadeType.ALL
        Medico savedMedico = medicoRepository.save(medico);
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
                .map(medico -> ResponseEntity.ok(new MedicoDTO(medico))) // Retorna o DTO do médico
                .orElse(ResponseEntity.notFound().build()); // Retorna 404 se não encontrar
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
