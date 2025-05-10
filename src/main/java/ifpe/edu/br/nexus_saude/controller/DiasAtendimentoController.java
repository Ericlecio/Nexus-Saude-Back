package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/DiasAtendimento")
public class DiasAtendimentoController {

    @Autowired
    private DiasAtendimentoRepository diasAtendimentoRepository;

    // Método para listar os dias de atendimento de um médico específico
    @GetMapping("/listar/{medicoId}")
    public ResponseEntity<List<DiasAtendimento>> getDiasAtendimento(@PathVariable Integer medicoId) {
        List<DiasAtendimento> diasAtendimentos = diasAtendimentoRepository.findByMedicoId(medicoId);
        return ResponseEntity.ok(diasAtendimentos);
    }
}
