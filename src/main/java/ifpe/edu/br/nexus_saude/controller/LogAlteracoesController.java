package ifpe.edu.br.nexus_saude.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifpe.edu.br.nexus_saude.dto.LogAlteracoesDTO;
import ifpe.edu.br.nexus_saude.model.LogAlteracoes;
import ifpe.edu.br.nexus_saude.repository.LogAlteracoesRepository;

@RestController
@RequestMapping("/api/logs")
public class LogAlteracoesController {
	private final LogAlteracoesRepository logAlteracoesRepository;

    public LogAlteracoesController(LogAlteracoesRepository logAlteracoesRepository) {
        this.logAlteracoesRepository = logAlteracoesRepository;
    }

    @PostMapping
    public ResponseEntity<LogAlteracoesDTO> criarLog(@RequestBody LogAlteracoesDTO dto) {
        LogAlteracoes log = new LogAlteracoes(
                null,
                dto.getEntidade(),
                dto.getRegistroId(),
                dto.getUsuarioResponsavel(),
                LocalDateTime.now(), // Definindo a data automaticamente
                dto.getAlteracao()
        );

        LogAlteracoes savedLog = logAlteracoesRepository.save(log);

        LogAlteracoesDTO responseDTO = LogAlteracoesDTO.fromEntity(savedLog); //

        return ResponseEntity.ok(responseDTO);

}}
