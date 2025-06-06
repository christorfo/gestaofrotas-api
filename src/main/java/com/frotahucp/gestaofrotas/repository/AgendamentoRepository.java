package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.frotahucp.gestaofrotas.model.StatusAgendamento;
import com.frotahucp.gestaofrotas.model.Veiculo;
import com.frotahucp.gestaofrotas.model.Motorista;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Verifica se existe algum agendamento para o veículo com o status especificado
    boolean existsByVeiculoAndStatus(Veiculo veiculo, StatusAgendamento status);

    // Verifica se existe algum agendamento para o motorista com o status especificado
    boolean existsByMotoristaAndStatus(Motorista motorista, StatusAgendamento status);
}