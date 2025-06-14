package ifpe.edu.br.nexus_saude.controller;

import ifpe.edu.br.nexus_saude.dto.DiasAtendimentoDTO;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/DiasAtendimento")
public class DiasAtendimentoController {

    @Autowired
    private DiasAtendimentoRepository diasAtendimentoRepository;

    @GetMapping("/listar/{medicoId}")
    public ResponseEntity<List<DiasAtendimentoDTO>> getDiasAtendimento(@PathVariable Integer medicoId) {
        List<DiasAtendimento> diasAtendimentos = diasAtendimentoRepository.findByMedicoId(medicoId);
        List<DiasAtendimentoDTO> diasDTOs = diasAtendimentos.stream()
                .map(DiasAtendimentoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diasDTOs);
    }
}
