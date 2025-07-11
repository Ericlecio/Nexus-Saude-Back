package ifpe.edu.br.nexus_saude.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// import ifpe.edu.br.nexus_saude.dto.AgendamentoDTO;
import ifpe.edu.br.nexus_saude.model.Agendamento;
import ifpe.edu.br.nexus_saude.model.Paciente;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer> {
    List<Agendamento> findByMedicoIdAndDataAfter(Integer medicoId, LocalDateTime data);

    boolean existsByMedicoIdAndData(Integer medicoId, LocalDateTime data);

    boolean existsByMedicoIdAndDataAndAgendamentoIdNot(Integer medicoId, LocalDateTime data, Integer agendamentoId);

    List<Agendamento> findByMedicoId(Integer medicoId);

    List<Agendamento> findByPaciente(Paciente paciente);

}