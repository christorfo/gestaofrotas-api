package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.OcorrenciaRequest;
import com.frotahucp.gestaofrotas.dto.OcorrenciaResponse;
import com.frotahucp.gestaofrotas.model.*;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import com.frotahucp.gestaofrotas.repository.OcorrenciaRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OcorrenciaService {

    @Autowired
    private OcorrenciaRepository ocorrenciaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Transactional
    public OcorrenciaResponse registrarOcorrencia(OcorrenciaRequest request, UserDetails currentUser) {
        // 1. Busca o motorista autenticado
        Motorista motorista = motoristaRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado para o usuário autenticado."));

        // 2. Busca o veículo informado
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + request.getVeiculoId()));

        // 3. Cria e salva a nova ocorrência
        Ocorrencia ocorrencia = new Ocorrencia();
        ocorrencia.setMotorista(motorista);
        ocorrencia.setVeiculo(veiculo);
        ocorrencia.setDescricao(request.getDescricao());
        ocorrencia.setDataHoraRegistro(LocalDateTime.now());
        ocorrencia.setStatus(StatusOcorrencia.ABERTA);

        Ocorrencia salva = ocorrenciaRepository.save(ocorrencia);
        return new OcorrenciaResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<OcorrenciaResponse> listarTodasOcorrencias() {
        List<Ocorrencia> ocorrencias = ocorrenciaRepository.findAllByOrderByDataHoraRegistroDesc();
        return ocorrencias.stream()
                .map(OcorrenciaResponse::new)
                .collect(Collectors.toList());
    }

}