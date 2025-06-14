package com.frotahucp.gestaofrotas.dto;

import com.frotahucp.gestaofrotas.model.Agendamento;
import com.frotahucp.gestaofrotas.model.StatusAgendamento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AgendamentoResponse {

    // Sub-DTO (record) para o histórico, para uma resposta mais limpa
    public record HistoricoDto(
            String statusNovo,
            LocalDateTime dataHoraAlteracao, // Sem @JsonFormat
            String usuarioResponsavel) {
    }

    // Sub-DTO (record) para os resumos
    public record VeiculoResumoDto(Long id, String placa, int quilometragemAtual) {
    }

    public record MotoristaResumoDto(Long id, String nome) {
    }

    // Campos da Resposta Principal
    private Long id;
    private VeiculoResumoDto veiculo;
    private MotoristaResumoDto motorista;
    private LocalDateTime dataHoraSaida; // Sem @JsonFormat
    private String destino;
    private StatusAgendamento status;
    private String justificativa;
    private String observacoesSaida;
    private String observacoesRetorno;
    private Integer quilometragemSaida;
    private Integer quilometragemFinal;
    private LocalDateTime dataHoraInicioViagem; // Sem @JsonFormat
    private LocalDateTime dataHoraRetorno; // Sem @JsonFormat
    private List<HistoricoDto> historicoStatus;

    // Construtor que mapeia a Entidade para este DTO
    public AgendamentoResponse(Agendamento agendamento) {
        this.id = agendamento.getId();
        this.veiculo = new VeiculoResumoDto(agendamento.getVeiculo().getId(), agendamento.getVeiculo().getPlaca(),
                agendamento.getVeiculo().getQuilometragemAtual());
        this.motorista = new MotoristaResumoDto(agendamento.getMotorista().getId(),
                agendamento.getMotorista().getNomeCompleto());
        this.dataHoraSaida = agendamento.getDataHoraSaida();
        this.destino = agendamento.getDestino();
        this.status = agendamento.getStatus();
        this.justificativa = agendamento.getJustificativa();
        this.observacoesSaida = agendamento.getObservacoesSaida();
        this.observacoesRetorno = agendamento.getObservacoesRetorno();
        this.quilometragemSaida = agendamento.getQuilometragemSaida();
        this.quilometragemFinal = agendamento.getQuilometragemFinal();
        this.dataHoraInicioViagem = agendamento.getDataHoraInicioViagem();
        this.dataHoraRetorno = agendamento.getDataHoraRetorno();

        this.historicoStatus = agendamento.getHistoricoStatus().stream()
                .map(h -> new HistoricoDto(
                        h.getStatusNovo().toString(),
                        h.getDataHoraAlteracao(),
                        h.getUsuarioResponsavel()))
                .collect(Collectors.toList());
    }

    // Getters para todos os campos
    public Long getId() {
        return id;
    }

    public VeiculoResumoDto getVeiculo() {
        return veiculo;
    }

    public MotoristaResumoDto getMotorista() {
        return motorista;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public String getDestino() {
        return destino;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public String getObservacoesSaida() {
        return observacoesSaida;
    }

    public String getObservacoesRetorno() {
        return observacoesRetorno;
    }

    public Integer getQuilometragemSaida() {
        return quilometragemSaida;
    }

    public Integer getQuilometragemFinal() {
        return quilometragemFinal;
    }

    public LocalDateTime getDataHoraInicioViagem() {
        return dataHoraInicioViagem;
    }

    public LocalDateTime getDataHoraRetorno() {
        return dataHoraRetorno;
    }

    public List<HistoricoDto> getHistoricoStatus() {
        return historicoStatus;
    }
}