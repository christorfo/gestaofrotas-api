package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Agendamento;
import com.frotahucp.gestaofrotas.model.Motorista;
import com.frotahucp.gestaofrotas.model.StatusAgendamento;
import com.frotahucp.gestaofrotas.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>, JpaSpecificationExecutor<Agendamento> {
    // Verifica se existe algum agendamento para o veículo com o status especificado
    boolean existsByVeiculoAndStatus(Veiculo veiculo, StatusAgendamento status);

    // Verifica se existe algum agendamento para o motorista com o status especificado
    boolean existsByMotoristaAndStatus(Motorista motorista, StatusAgendamento status);

    // NOVO MÉTODO: Encontra todos os agendamentos para um motorista, ordenados pela data/hora de saída
    List<Agendamento> findByMotoristaOrderByDataHoraSaidaAsc(Motorista motorista);
}