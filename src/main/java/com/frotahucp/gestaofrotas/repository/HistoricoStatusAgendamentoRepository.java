package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.HistoricoStatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoStatusAgendamentoRepository extends JpaRepository<HistoricoStatusAgendamento, Long> {
}