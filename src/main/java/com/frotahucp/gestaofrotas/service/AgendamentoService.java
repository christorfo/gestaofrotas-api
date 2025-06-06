package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.model.*;
import com.frotahucp.gestaofrotas.repository.AgendamentoRepository;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;

import com.frotahucp.gestaofrotas.dto.FinalizarViagemRequest;
import com.frotahucp.gestaofrotas.dto.IniciarViagemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import org.springframework.security.core.userdetails.UserDetails;

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

        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL || 
            agendamentoRepository.existsByVeiculoAndStatus(veiculo, StatusAgendamento.EM_USO)) {
            throw new RuntimeException("Veículo com placa " + veiculo.getPlaca() + " não está disponível para agendamento.");
        }

        // 2. Validar e buscar o Motorista
        Motorista motorista = motoristaRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + request.getMotoristaId()));

        if (motorista.getStatus() != StatusUsuario.ATIVO ||
            agendamentoRepository.existsByMotoristaAndStatus(motorista, StatusAgendamento.EM_USO)) {
            throw new RuntimeException("Motorista " + motorista.getNomeCompleto() + " não está disponível para agendamento.");
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

        @Transactional
    public Agendamento iniciarViagem(Long agendamentoId, IniciarViagemRequest request, UserDetails userDetails) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        // Validação 1: O motorista autenticado é o mesmo do agendamento?
        if (!agendamento.getMotorista().getEmail().equals(userDetails.getUsername())) {
            throw new RuntimeException("Acesso negado: Você não é o motorista designado para esta viagem.");
        }

        // Validação 2: O agendamento está no status correto?
        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Esta viagem não pode ser iniciada. Status atual: " + agendamento.getStatus());
        }

        // Validação 3: A quilometragem de saída é válida?
        if (request.getQuilometragemSaida() < agendamento.getVeiculo().getQuilometragemAtual()) {
            throw new RuntimeException("A quilometragem de saída não pode ser menor que a quilometragem atual do veículo.");
        }

        // Atualizar o Agendamento
        agendamento.setStatus(StatusAgendamento.EM_USO);
        agendamento.setDataHoraInicioViagem(LocalDateTime.now());
        agendamento.setQuilometragemSaida(request.getQuilometragemSaida());
        agendamento.setObservacoesSaida(request.getObservacoesSaida());

        // Atualizar a quilometragem do Veículo
        agendamento.getVeiculo().setQuilometragemAtual(request.getQuilometragemSaida());

        return agendamentoRepository.save(agendamento);
    }

    @Transactional
    public Agendamento finalizarViagem(Long agendamentoId, FinalizarViagemRequest request, UserDetails userDetails) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        // Validação 1: O motorista autenticado é o mesmo do agendamento?
        if (!agendamento.getMotorista().getEmail().equals(userDetails.getUsername())) {
            throw new RuntimeException("Acesso negado: Você não é o motorista designado para esta viagem.");
        }

        // Validação 2: O agendamento está no status correto?
        if (agendamento.getStatus() != StatusAgendamento.EM_USO) {
            throw new RuntimeException("Esta viagem não pode ser finalizada. Status atual: " + agendamento.getStatus());
        }

        // Validação 3: A quilometragem final é válida?
        if (request.getQuilometragemFinal() <= agendamento.getQuilometragemSaida()) {
            throw new RuntimeException("A quilometragem final deve ser maior que a quilometragem de saída.");
        }

        // Atualizar o Agendamento
        agendamento.setStatus(StatusAgendamento.FINALIZADO);
        agendamento.setDataHoraRetorno(LocalDateTime.now());
        agendamento.setQuilometragemFinal(request.getQuilometragemFinal());
        agendamento.setObservacoesRetorno(request.getObservacoesRetorno());

        // Atualizar a quilometragem do Veículo
        agendamento.getVeiculo().setQuilometragemAtual(request.getQuilometragemFinal());

        return agendamentoRepository.save(agendamento);
    }
}