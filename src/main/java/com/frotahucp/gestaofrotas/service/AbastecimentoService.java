package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.AbastecimentoRequest;
import com.frotahucp.gestaofrotas.dto.AbastecimentoResponse;
import com.frotahucp.gestaofrotas.model.Abastecimento;
import com.frotahucp.gestaofrotas.model.Veiculo;
import com.frotahucp.gestaofrotas.repository.AbastecimentoRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbastecimentoService {

    @Autowired
    private AbastecimentoRepository abastecimentoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Transactional
    public AbastecimentoResponse registrarAbastecimento(AbastecimentoRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + request.getVeiculoId()));

        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setVeiculo(veiculo);
        abastecimento.setData(request.getData());
        abastecimento.setTipoCombustivel(request.getTipoCombustivel());
        abastecimento.setValor(request.getValor());
        abastecimento.setQuilometragem(request.getQuilometragem());
        abastecimento.setMotoristaResponsavel(request.getMotoristaResponsavel());

        Abastecimento salvo = abastecimentoRepository.save(abastecimento);
        return new AbastecimentoResponse(salvo);
    }
}