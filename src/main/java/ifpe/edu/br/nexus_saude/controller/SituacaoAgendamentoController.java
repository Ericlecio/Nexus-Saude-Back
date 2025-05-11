package ifpe.edu.br.nexus_saude.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.model.SituacaoAgendamento;
import ifpe.edu.br.nexus_saude.repository.SituacaoAgendamentoRepository;

@RestController
@RequestMapping("/situacoes")
public class SituacaoAgendamentoController {
	@Autowired
    private SituacaoAgendamentoRepository situacaoRepository;

    // 🔹 GET: List all Situacoes
    @GetMapping
    public List<SituacaoAgendamento> listarSituacoes() {
        return situacaoRepository.findAll();
    }

    // 🔹 GET: Get Situacao by ID
    @GetMapping("/{id}")
    public ResponseEntity<SituacaoAgendamento> obterSituacaoPorId(@PathVariable Integer id) {
        Optional<SituacaoAgendamento> situacao = situacaoRepository.findById(id);
        return situacao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 POST: Create a new Situacao
    @PostMapping
    public ResponseEntity<SituacaoAgendamento> criarSituacao(@RequestBody SituacaoAgendamento situacao) {
        SituacaoAgendamento savedSituacao = situacaoRepository.save(situacao);
        return ResponseEntity.ok(savedSituacao);
    }

    // 🔹 PUT: Update an existing Situacao
    @PutMapping("/{id}")
    public ResponseEntity<SituacaoAgendamento> atualizarSituacao(@PathVariable Integer id, @RequestBody SituacaoAgendamento situacaoAtualizada) {
        return situacaoRepository.findById(id)
                .map(situacao -> {
                    situacao.setDescricao(situacaoAtualizada.getDescricao());
                    SituacaoAgendamento updatedSituacao = situacaoRepository.save(situacao);
                    return ResponseEntity.ok(updatedSituacao);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 DELETE: Remove a Situacao
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarSituacao(@PathVariable Integer id) {
        return situacaoRepository.findById(id)
                .map(situacao -> {
                    situacaoRepository.delete(situacao);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
