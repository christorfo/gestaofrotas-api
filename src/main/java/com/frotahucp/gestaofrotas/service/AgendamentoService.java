package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.model.*;
import com.frotahucp.gestaofrotas.repository.AgendamentoRepository;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Transactional
    public Agendamento criarAgendamento(AgendamentoRequest request) {
        // 1. Validar e buscar o Veículo
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + request.getVeiculoId()));

        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL) { // O status do veículo é verificado 
            throw new RuntimeException("Veículo com placa " + veiculo.getPlaca() + " não está disponível para agendamento.");
        }

        // 2. Validar e buscar o Motorista
        Motorista motorista = motoristaRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + request.getMotoristaId()));

        if (motorista.getStatus() != StatusUsuario.ATIVO) {
            throw new RuntimeException("Motorista " + motorista.getNomeCompleto() + " não está ativo.");
        }

        // Adicionar validações de conflito de horário aqui se necessário

        // 3. Criar e salvar o Agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setVeiculo(veiculo);
        agendamento.setMotorista(motorista);
        agendamento.setDataHoraSaida(request.getDataHoraSaida());
        agendamento.setDestino(request.getDestino());
        agendamento.setJustificativa(request.getJustificativa());
        agendamento.setStatus(StatusAgendamento.AGENDADO); // O status inicial do agendamento é definido como AGENDADO 

        return agendamentoRepository.save(agendamento);
    }
}