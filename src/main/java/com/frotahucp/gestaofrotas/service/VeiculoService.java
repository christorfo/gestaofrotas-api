package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.model.Veiculo;
import com.frotahucp.gestaofrotas.model.StatusVeiculo;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import com.frotahucp.gestaofrotas.model.StatusVeiculo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Importe uma exceção personalizada ou use uma existente, como RuntimeException por enquanto.
// Ex: import com.frotahucp.gestaofrotas.exception.ResourceNotFoundException;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    @Autowired
    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional
    public Veiculo cadastrarVeiculo(Veiculo veiculo) {
        // Poderia adicionar validações de negócio aqui antes de salvar.
        // Por exemplo, verificar se a placa já existe, embora o BD já tenha uma
        // constraint unique.
        // Se o status não for fornecido, poderia definir um padrão, ex: DISPONIVEL.
        // RF010 define os campos obrigatórios: Placa, Modelo, Tipo, Ano, Quilometragem
        // atual, Status [cite: 23]
        return veiculoRepository.save(veiculo);
    }

    @Transactional(readOnly = true)
    public List<Veiculo> listarTodosVeiculos() {
        return veiculoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Veiculo> buscarVeiculoPorId(Long id) {
        return veiculoRepository.findById(id);
    }

    @Transactional
    public Veiculo editarVeiculo(Long id, Veiculo veiculoDetalhes) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + id)); // Substituir por
                                                                                                    // exceção mais
                                                                                                    // específica

        veiculoExistente.setPlaca(veiculoDetalhes.getPlaca());
        veiculoExistente.setModelo(veiculoDetalhes.getModelo());
        veiculoExistente.setTipo(veiculoDetalhes.getTipo());
        veiculoExistente.setAno(veiculoDetalhes.getAno());
        veiculoExistente.setQuilometragemAtual(veiculoDetalhes.getQuilometragemAtual());
        veiculoExistente.setStatus(veiculoDetalhes.getStatus());
        // Adicionar outras atualizações de campos conforme necessário

        return veiculoRepository.save(veiculoExistente);
    }

    @Transactional
    public Veiculo inativarVeiculo(Long id) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + id)); // Substituir por
                                                                                                    // exceção mais
                                                                                                    // específica

        veiculoExistente.setStatus(StatusVeiculo.INATIVO);
        return veiculoRepository.save(veiculoExistente);
    }

    // O RF010 [cite: 22] também menciona "Status (Disponível, Inativo ou Em
    // Manutenção)"[cite: 23].
    // Se precisar de uma funcionalidade para colocar "Em Manutenção" ou
    // "Disponível" explicitamente:
    @Transactional
    public Veiculo atualizarStatusVeiculo(Long id, StatusVeiculo novoStatus) {
        Veiculo veiculoExistente = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + id)); // Substituir por
                                                                                                    // exceção mais
                                                                                                    // específica
        veiculoExistente.setStatus(novoStatus);
        return veiculoRepository.save(veiculoExistente);
    }

    @Transactional
    public Veiculo liberarVeiculoDaManutencao(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + id));

        if (veiculo.getStatus() != StatusVeiculo.EM_MANUTENCAO) {
            throw new RuntimeException("Ação não permitida: O veículo não está em manutenção.");
        }

        veiculo.setStatus(StatusVeiculo.DISPONIVEL);
        return veiculoRepository.save(veiculo);
    }
}