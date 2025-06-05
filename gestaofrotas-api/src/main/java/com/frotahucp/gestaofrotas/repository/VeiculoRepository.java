package com.frotahucp.gestaofrotas.repository;

import com.frotahucp.gestaofrotas.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    // O JpaRepository já fornece métodos como:
    // - save(Veiculo entity) para criar ou atualizar um veículo
    // - findById(Long id) para buscar um veículo pelo ID
    // - findAll() para listar todos os veículos
    // - deleteById(Long id) para remover um veículo
    // - count() para contar o número de veículos
    // - existsById(Long id) para verificar se um veículo existe

    // Exemplo de um método de consulta customizado (se necessário no futuro):
    // Optional<Veiculo> findByPlaca(String placa);

}