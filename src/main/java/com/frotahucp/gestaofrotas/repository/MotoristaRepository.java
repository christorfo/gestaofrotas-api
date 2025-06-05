package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Motorista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, Long> {
    Optional<Motorista> findByEmail(String email); // Útil para login
    Optional<Motorista> findByCpf(String cpf);   // Para verificar unicidade
}