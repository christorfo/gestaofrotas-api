package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
    List<Manutencao> findByVeiculoIdOrderByDataDesc(Long veiculoId);
}