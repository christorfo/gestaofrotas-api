package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.dto.AgendamentoResponse;
import com.frotahucp.gestaofrotas.dto.FinalizarViagemRequest;
import com.frotahucp.gestaofrotas.dto.IniciarViagemRequest;
import com.frotahucp.gestaofrotas.model.*;
import com.frotahucp.gestaofrotas.repository.AgendamentoRepository;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification; 
import jakarta.persistence.criteria.Predicate; 
import java.time.LocalDate; 
import java.util.ArrayList; 
import java.util.List; 
import java.util.stream.Collectors; 

import java.time.LocalDateTime;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Transactional
    public AgendamentoResponse criarAgendamento(AgendamentoRequest request) {
        // 1. Validar e buscar o Veículo
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + request.getVeiculoId()));

        // Validação de disponibilidade do Veículo
        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL ||
            agendamentoRepository.existsByVeiculoAndStatus(veiculo, StatusAgendamento.EM_USO)) {
            throw new RuntimeException("Veículo com placa " + veiculo.getPlaca() + " não está disponível para agendamento.");
        }

        // 2. Validar e buscar o Motorista
        Motorista motorista = motoristaRepository.findById(request.getMotoristaId())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + request.getMotoristaId()));

        // Validação de disponibilidade do Motorista
        if (motorista.getStatus() != StatusUsuario.ATIVO ||
            agendamentoRepository.existsByMotoristaAndStatus(motorista, StatusAgendamento.EM_USO)) {
            throw new RuntimeException("Motorista " + motorista.getNomeCompleto() + " não está disponível para agendamento.");
        }

        // 3. Criar e salvar o Agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setVeiculo(veiculo);
        agendamento.setMotorista(motorista);
        agendamento.setDataHoraSaida(request.getDataHoraSaida());
        agendamento.setDestino(request.getDestino());
        agendamento.setJustificativa(request.getJustificativa());
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return new AgendamentoResponse(agendamentoSalvo);
    }

    @Transactional
    public AgendamentoResponse iniciarViagem(Long agendamentoId, IniciarViagemRequest request, UserDetails userDetails) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (!agendamento.getMotorista().getEmail().equals(userDetails.getUsername())) {
            throw new RuntimeException("Acesso negado: Você não é o motorista designado para esta viagem.");
        }

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Esta viagem não pode ser iniciada. Status atual: " + agendamento.getStatus());
        }

        if (request.getQuilometragemSaida() < agendamento.getVeiculo().getQuilometragemAtual()) {
            throw new RuntimeException("A quilometragem de saída não pode ser menor que a quilometragem atual do veículo.");
        }

        agendamento.setStatus(StatusAgendamento.EM_USO);
        agendamento.setDataHoraInicioViagem(LocalDateTime.now());
        agendamento.setQuilometragemSaida(request.getQuilometragemSaida());
        agendamento.setObservacoesSaida(request.getObservacoesSaida());
        agendamento.getVeiculo().setQuilometragemAtual(request.getQuilometragemSaida());

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return new AgendamentoResponse(agendamentoSalvo);
    }

    @Transactional
    public AgendamentoResponse finalizarViagem(Long agendamentoId, FinalizarViagemRequest request, UserDetails userDetails) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (!agendamento.getMotorista().getEmail().equals(userDetails.getUsername())) {
            throw new RuntimeException("Acesso negado: Você não é o motorista designado para esta viagem.");
        }

        if (agendamento.getStatus() != StatusAgendamento.EM_USO) {
            throw new RuntimeException("Esta viagem não pode ser finalizada. Status atual: " + agendamento.getStatus());
        }

        if (request.getQuilometragemFinal() <= agendamento.getQuilometragemSaida()) {
            throw new RuntimeException("A quilometragem final deve ser maior que a quilometragem de saída.");
        }

        agendamento.setStatus(StatusAgendamento.FINALIZADO);
        agendamento.setDataHoraRetorno(LocalDateTime.now());
        agendamento.setQuilometragemFinal(request.getQuilometragemFinal());
        agendamento.setObservacoesRetorno(request.getObservacoesRetorno());
        agendamento.getVeiculo().setQuilometragemAtual(request.getQuilometragemFinal());

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return new AgendamentoResponse(agendamentoSalvo);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAgendamentosFiltrados(
            LocalDate dataInicio, LocalDate dataFim, Long motoristaId, StatusAgendamento status) {
        
        // Specification é usada para construir a query dinamicamente
        Specification<Agendamento> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dataInicio != null) {
                // Adiciona filtro: data/hora de saída >= data de início fornecida
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraSaida"), dataInicio.atStartOfDay()));
            }
            if (dataFim != null) {
                // Adiciona filtro: data/hora de saída <= data de fim fornecida
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraSaida"), dataFim.atTime(23, 59, 59)));
            }
            if (motoristaId != null) {
                // Adiciona filtro: o ID do motorista associado deve ser igual ao fornecido
                predicates.add(criteriaBuilder.equal(root.get("motorista").get("id"), motoristaId));
            }
            if (status != null) {
                // Adiciona filtro: o status do agendamento deve ser igual ao fornecido
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // Combina todos os predicados (filtros) com um "AND"
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Usa o repositório para encontrar todos os agendamentos que correspondem à especificação
        List<Agendamento> agendamentos = agendamentoRepository.findAll(spec);
        
        // Mapeia a lista de entidades para uma lista de DTOs de resposta
        return agendamentos.stream()
                           .map(AgendamentoResponse::new)
                           .collect(Collectors.toList());
    }    
}