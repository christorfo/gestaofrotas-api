package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.dto.AgendamentoResponse;
import com.frotahucp.gestaofrotas.dto.FinalizarViagemRequest;
import com.frotahucp.gestaofrotas.dto.IniciarViagemRequest;
import com.frotahucp.gestaofrotas.model.*;
import com.frotahucp.gestaofrotas.repository.AgendamentoRepository;
import com.frotahucp.gestaofrotas.repository.HistoricoStatusAgendamentoRepository;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import com.frotahucp.gestaofrotas.repository.VeiculoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private HistoricoStatusAgendamentoRepository historicoRepository;

    private void registrarHistorico(Agendamento agendamento, StatusAgendamento statusAnterior,
            StatusAgendamento statusNovo, String usuarioEmail) {
        HistoricoStatusAgendamento historico = new HistoricoStatusAgendamento(agendamento, statusAnterior, statusNovo,
                usuarioEmail);
        historicoRepository.save(historico);
    }

    @Transactional
    public AgendamentoResponse criarAgendamento(AgendamentoRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com o ID: " + request.getVeiculoId()));

        if (veiculo.getStatus() != StatusVeiculo.DISPONIVEL ||
                agendamentoRepository.existsByVeiculoAndStatus(veiculo, StatusAgendamento.EM_USO)) {
            throw new RuntimeException(
                    "Veículo com placa " + veiculo.getPlaca() + " não está disponível para agendamento.");
        }

        Motorista motorista = motoristaRepository.findById(request.getMotoristaId())
                .orElseThrow(
                        () -> new RuntimeException("Motorista não encontrado com o ID: " + request.getMotoristaId()));

        if (motorista.getStatus() != StatusUsuario.ATIVO ||
                agendamentoRepository.existsByMotoristaAndStatus(motorista, StatusAgendamento.EM_USO)) {
            throw new RuntimeException(
                    "Motorista " + motorista.getNomeCompleto() + " não está disponível para agendamento.");
        }

        Agendamento agendamento = new Agendamento();
        agendamento.setVeiculo(veiculo);
        agendamento.setMotorista(motorista);
        agendamento.setDataHoraSaida(request.getDataHoraSaida());
        agendamento.setDestino(request.getDestino());
        agendamento.setJustificativa(request.getJustificativa());
        agendamento.setStatus(StatusAgendamento.AGENDADO);

        // O @Transactional garante que o agendamento tenha um ID antes de registrar o
        // histórico
        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        registrarHistorico(agendamentoSalvo, null, StatusAgendamento.AGENDADO, "ADMINISTRADOR_SISTEMA"); // TODO: Obter
                                                                                                         // admin logado

        return new AgendamentoResponse(agendamentoSalvo);
    }

    @Transactional
    public AgendamentoResponse iniciarViagem(Long agendamentoId, IniciarViagemRequest request,
            UserDetails userDetails) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (!agendamento.getMotorista().getEmail().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Acesso negado: Você não é o motorista designado para esta viagem.");
        }
        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new RuntimeException("Esta viagem não pode ser iniciada. Status atual: " + agendamento.getStatus());
        }
        if (request.getQuilometragemSaida() < agendamento.getVeiculo().getQuilometragemAtual()) {
            throw new RuntimeException(
                    "A quilometragem de saída não pode ser menor que a quilometragem atual do veículo.");
        }

        StatusAgendamento statusAnterior = agendamento.getStatus();
        agendamento.setStatus(StatusAgendamento.EM_USO);
        agendamento.setDataHoraInicioViagem(LocalDateTime.now());
        agendamento.setQuilometragemSaida(request.getQuilometragemSaida());
        agendamento.setObservacoesSaida(request.getObservacoesSaida());
        agendamento.getVeiculo().setQuilometragemAtual(request.getQuilometragemSaida());

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        registrarHistorico(agendamentoSalvo, statusAnterior, agendamentoSalvo.getStatus(), userDetails.getUsername());

        return new AgendamentoResponse(agendamentoSalvo);
    }

    @Transactional
    public AgendamentoResponse finalizarViagem(Long agendamentoId, FinalizarViagemRequest request,
            UserDetails userDetails) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        if (!agendamento.getMotorista().getEmail().equals(userDetails.getUsername())) {
            throw new AccessDeniedException("Acesso negado: Você não é o motorista designado para esta viagem.");
        }
        if (agendamento.getStatus() != StatusAgendamento.EM_USO) {
            throw new RuntimeException("Esta viagem não pode ser finalizada. Status atual: " + agendamento.getStatus());
        }
        if (request.getQuilometragemFinal() <= agendamento.getQuilometragemSaida()) {
            throw new RuntimeException("A quilometragem final deve ser maior que a quilometragem de saída.");
        }

        StatusAgendamento statusAnterior = agendamento.getStatus();
        agendamento.setStatus(StatusAgendamento.FINALIZADO);
        agendamento.setDataHoraRetorno(LocalDateTime.now());
        agendamento.setQuilometragemFinal(request.getQuilometragemFinal());
        agendamento.setObservacoesRetorno(request.getObservacoesRetorno());
        agendamento.getVeiculo().setQuilometragemAtual(request.getQuilometragemFinal());

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        registrarHistorico(agendamentoSalvo, statusAnterior, agendamentoSalvo.getStatus(), userDetails.getUsername());

        return new AgendamentoResponse(agendamentoSalvo);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAgendamentosFiltrados(LocalDate dataInicio, LocalDate dataFim,
            Long motoristaId, StatusAgendamento status) {
        Specification<Agendamento> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dataInicio != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraSaida"), dataInicio.atStartOfDay()));
            }
            if (dataFim != null) {
                predicates
                        .add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraSaida"), dataFim.atTime(23, 59, 59)));
            }
            if (motoristaId != null) {
                predicates.add(criteriaBuilder.equal(root.get("motorista").get("id"), motoristaId));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        List<Agendamento> agendamentos = agendamentoRepository.findAll(spec);
        return agendamentos.stream().map(AgendamentoResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AgendamentoResponse buscarAgendamentoDetalhado(Long agendamentoId, UserDetails currentUser) {
        Agendamento agendamento = agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new RuntimeException("Agendamento não encontrado com o ID: " + agendamentoId));

        boolean isAdministrador = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR"));
        boolean isMotoristaDono = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MOTORISTA")) &&
                agendamento.getMotorista().getEmail().equals(currentUser.getUsername());

        if (!isAdministrador && !isMotoristaDono) {
            throw new AccessDeniedException("Você não tem permissão para visualizar este agendamento.");
        }
        return new AgendamentoResponse(agendamento);
    }

    @Transactional(readOnly = true)
    public List<AgendamentoResponse> listarAgendamentosDoMotorista(UserDetails userDetails, StatusAgendamento status) {
        Motorista motorista = motoristaRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado para o usuário autenticado."));
        List<Agendamento> agendamentos;
        if (status != null) {
            agendamentos = agendamentoRepository.findByMotoristaAndStatusOrderByDataHoraSaidaAsc(motorista, status);
        } else {
            agendamentos = agendamentoRepository.findByMotoristaOrderByDataHoraSaidaAsc(motorista);
        }
        return agendamentos.stream().map(AgendamentoResponse::new).collect(Collectors.toList());
    }
}