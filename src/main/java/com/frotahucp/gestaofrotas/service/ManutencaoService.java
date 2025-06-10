package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.ManutencaoRequest;
import com.frotahucp.gestaofrotas.dto.ManutencaoResponse;
import com.frotahucp.gestaofrotas.model.Manutencao;
import com.frotahucp.gestaofrotas.model.StatusVeiculo;
import com.frotahucp.gestaofrotas.model.Veiculo;
import com.frotahucp.gestaofrotas.repository.ManutencaoRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManutencaoService {

    @Autowired
    private ManutencaoRepository manutencaoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional
    public ManutencaoResponse registrarManutencao(ManutencaoRequest request) {
        // 1. Busca o veículo
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + request.getVeiculoId()));

        // 2. Atualiza o status do veículo para EM_MANUTENCAO
        veiculo.setStatus(StatusVeiculo.EM_MANUTENCAO);
        // Opcional: Atualizar a quilometragem do veículo se a da manutenção for maior
        if (request.getQuilometragem() > veiculo.getQuilometragemAtual()) {
            veiculo.setQuilometragemAtual(request.getQuilometragem());
        }

        // 3. Cria e salva o novo registro de manutenção
        Manutencao manutencao = new Manutencao();
        manutencao.setVeiculo(veiculo);
        manutencao.setData(request.getData());
        manutencao.setTipo(request.getTipo());
        manutencao.setDescricao(request.getDescricao());
        manutencao.setValor(request.getValor());
        manutencao.setQuilometragem(request.getQuilometragem());

        Manutencao salva = manutencaoRepository.save(manutencao);

        // A anotação @Transactional garante que a alteração no veículo também será
        // salva

        return new ManutencaoResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarManutencoesPorVeiculo(Long veiculoId) {
        // Valida se o veículo existe
        veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + veiculoId));

        List<Manutencao> manutencoes = manutencaoRepository.findByVeiculoIdOrderByDataDesc(veiculoId);

        return manutencoes.stream()
                .map(ManutencaoResponse::new)
                .collect(Collectors.toList());
    }
}