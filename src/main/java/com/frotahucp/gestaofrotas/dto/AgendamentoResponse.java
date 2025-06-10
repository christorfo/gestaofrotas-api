package com.frotahucp.gestaofrotas.dto;

import com.frotahucp.gestaofrotas.model.Agendamento;
import com.frotahucp.gestaofrotas.model.StatusAgendamento;

import java.time.LocalDateTime;

public class AgendamentoResponse {

    private Long id;
    private VeiculoResumoDto veiculo;
    private MotoristaResumoDto motorista;
    private LocalDateTime dataHoraSaida;
    private String destino;
    private StatusAgendamento status;
    private LocalDateTime dataHoraInicioViagem;
    private Integer quilometragemSaida;
    private LocalDateTime dataHoraRetorno;
    private Integer quilometragemFinal;

    // Construtor que mapeia a Entidade para o DTO
    public AgendamentoResponse(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.veiculo = new VeiculoResumoDto(agendamento.getVeiculo().getId(), agendamento.getVeiculo().getPlaca(), agendamento.getVeiculo().getQuilometragemAtual());
        this.motorista = new MotoristaResumoDto(agendamento.getMotorista().getId(), agendamento.getMotorista().getNomeCompleto());
        this.dataHoraSaida = agendamento.getDataHoraSaida();
        this.destino = agendamento.getDestino();
        this.status = agendamento.getStatus();
        this.dataHoraInicioViagem = agendamento.getDataHoraInicioViagem();
        this.quilometragemSaida = agendamento.getQuilometragemSaida();
        this.dataHoraRetorno = agendamento.getDataHoraRetorno();
        this.quilometragemFinal = agendamento.getQuilometragemFinal();
    }

    // Sub-DTOs para resumos (evita expor a entidade inteira)
    public record VeiculoResumoDto(Long id, String placa, int quilometragemAtual) {}
    public record MotoristaResumoDto(Long id, String nome) {}

    // Getters
    public Long getId() { return id; }
    public VeiculoResumoDto getVeiculo() { return veiculo; }
    public MotoristaResumoDto getMotorista() { return motorista; }
    public LocalDateTime getDataHoraSaida() { return dataHoraSaida; }
    public String getDestino() { return destino; }
    public StatusAgendamento getStatus() { return status; }
    public LocalDateTime getDataHoraInicioViagem() { return dataHoraInicioViagem; }
    public Integer getQuilometragemSaida() { return quilometragemSaida; }
    public LocalDateTime getDataHoraRetorno() { return dataHoraRetorno; }
    public Integer getQuilometragemFinal() { return quilometragemFinal; }
}