package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    List<Ocorrencia> findAllByOrderByDataHoraRegistroDesc();
}