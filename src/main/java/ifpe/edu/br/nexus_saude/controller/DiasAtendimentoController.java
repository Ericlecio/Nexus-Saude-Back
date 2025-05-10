package ifpe.edu.br.nexus_saude.controller;
import ifpe.edu.br.nexus_saude.dto.DiasAtendimentoDTO;
import ifpe.edu.br.nexus_saude.model.DiasAtendimento;
import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.DiasAtendimentoRepository;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api/dias-atendimento")
@CrossOrigin(origins = "http://localhost:5173")
public class DiasAtendimentoController {
	private final DiasAtendimentoRepository diasAtendimentoRepository;
	private final MedicoRepository medicoRepository;

	public DiasAtendimentoController(DiasAtendimentoRepository diasAtendimentoRepository, MedicoRepository medicoRepository) {
		this.diasAtendimentoRepository = diasAtendimentoRepository;
		this.medicoRepository = medicoRepository;
	}

	// 🔹 GET: List all DiasAtendimento
	@GetMapping
	public List<DiasAtendimentoDTO> listarDiasAtendimento() {
		return diasAtendimentoRepository.findAll()
				.stream()
				.map(dia -> new DiasAtendimentoDTO(
						dia.getDiasAtendimentoId(),
						dia.getMedico().getNome(),
						dia.getDiaSemana(),
						dia.getHorario(),
						dia.getCreatedAt(),
						dia.getUpdatedAt()
						))
				.toList();
	}

	// 🔹 GET: Get DiasAtendimento by ID
	@GetMapping("/{id}")
	public ResponseEntity<DiasAtendimentoDTO> obterDiasAtendimentoPorId(@PathVariable Integer id) {
		return diasAtendimentoRepository.findById(id)
				.map(dia -> ResponseEntity.ok(new DiasAtendimentoDTO(
						dia.getDiasAtendimentoId(),
						dia.getMedico().getNome(),
						dia.getDiaSemana(),
						dia.getHorario(),
						dia.getCreatedAt(),
						dia.getUpdatedAt()
						)))
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// 🔹 POST: Create a new DiasAtendimento
	@PostMapping
	public ResponseEntity<DiasAtendimentoDTO> criarDiasAtendimento(@RequestBody DiasAtendimentoDTO dto) {
		Optional<Medico> medico = medicoRepository.findById(dto.getDiasAtendimentoId());

		if (medico.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		DiasAtendimento diaAtendimento = new DiasAtendimento(
				null,
				medico.get(),
				dto.getDiaSemana(),
				dto.getHorario(),
				LocalDateTime.now(),
				LocalDateTime.now()
				);

		DiasAtendimento savedDiaAtendimento = diasAtendimentoRepository.save(diaAtendimento);
		return ResponseEntity.ok(new DiasAtendimentoDTO(
				savedDiaAtendimento.getDiasAtendimentoId(),
				savedDiaAtendimento.getMedico().getNome(),
				savedDiaAtendimento.getDiaSemana(),
				savedDiaAtendimento.getHorario(),
				savedDiaAtendimento.getCreatedAt(),
				savedDiaAtendimento.getUpdatedAt()
				));
	}

	// 🔹 PUT: Update an existing DiasAtendimento
	@PutMapping("/{id}")
	public ResponseEntity<DiasAtendimentoDTO> atualizarDiasAtendimento(@PathVariable Integer id, @RequestBody DiasAtendimentoDTO dto) {
	    Optional<DiasAtendimento> optionalDia = diasAtendimentoRepository.findById(id);

	    if (optionalDia.isEmpty()) {
	        return ResponseEntity.notFound().build(); // ✅ Handle 404 outside `.map()`
	    }

	    DiasAtendimento dia = optionalDia.get();
	    Optional<Medico> medico = medicoRepository.findById(dto.getDiasAtendimentoId());

	    if (medico.isEmpty()) {
	        return ResponseEntity.badRequest().build(); // ✅ Handle bad request outside `.map()`
	    }

	    dia.setMedico(medico.get());
	    dia.setDiaSemana(dto.getDiaSemana());
	    dia.setHorario(dto.getHorario());
	    dia.setUpdatedAt(LocalDateTime.now());

	    DiasAtendimento updatedDiaAtendimento = diasAtendimentoRepository.save(dia);
	    DiasAtendimentoDTO responseDTO = new DiasAtendimentoDTO(
	            updatedDiaAtendimento.getDiasAtendimentoId(),
	            updatedDiaAtendimento.getMedico().getNome(),
	            updatedDiaAtendimento.getDiaSemana(),
	            updatedDiaAtendimento.getHorario(),
	            updatedDiaAtendimento.getCreatedAt(),
	            updatedDiaAtendimento.getUpdatedAt()
	    );

	    return ResponseEntity.ok(responseDTO); // ✅ Proper return type!
	}
	// 🔹 DELETE: Remove DiasAtendimento
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarDiasAtendimento(@PathVariable Integer id) {
		return diasAtendimentoRepository.findById(id)
				.map(dia -> {
					diasAtendimentoRepository.delete(dia);
					return ResponseEntity.ok("Registro de Atendimento deletado com sucesso!");
				})
				.orElseGet(() -> ResponseEntity.status(404).body("Registro de Atendimento deletado"));
	}

}
