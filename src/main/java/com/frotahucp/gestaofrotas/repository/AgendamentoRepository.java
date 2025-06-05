package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    // Métodos de consulta personalizados podem ser adicionados aqui no futuro.
    // Por exemplo, para buscar agendamentos por motorista, veículo ou período.
}