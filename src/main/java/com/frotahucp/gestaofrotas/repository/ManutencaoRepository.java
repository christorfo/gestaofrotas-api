package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {
}