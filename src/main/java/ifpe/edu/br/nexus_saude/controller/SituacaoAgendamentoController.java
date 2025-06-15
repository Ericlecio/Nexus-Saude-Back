package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.SituacaoAgendamentoDTO;
import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import ifpe.edu.br.nexus_saude.repository.SituacaoAgendamentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/situacao")
public class SituacaoAgendamentoController {

    @Autowired
    private SituacaoAgendamentoRepository situacaoRepository;

    @GetMapping("/listar")
    public List<SituacaoAgendamentoDTO> listar() {
        return situacaoRepository.findAll()
                .stream()
                .map(SituacaoAgendamentoDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SituacaoAgendamentoDTO> obterPorId(@PathVariable Integer id) {
        Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(id);
        return situacao
                .map(s -> ResponseEntity.ok(new SituacaoAgendamentoDTO(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/inserir")

    @PutMapping("/update/{id}")
    public ResponseEntity<SituacaoAgendamentoDTO> atualizar(@PathVariable Integer id,
            @RequestBody SituacaoAgendamentoDTO dto) {
        return situacaoRepository.findById(id)
                .map(situacao -> {
                    situacao.setDescricao(dto.getDescricao());
                    SituacaoAgendamento atualizado = situacaoRepository.save(situacao);
                    return ResponseEntity.ok(new SituacaoAgendamentoDTO(atualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(id);
        if (situacao.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        situacaoRepository.delete(situacao.get());
        return ResponseEntity.noContent().build();
    }
}
