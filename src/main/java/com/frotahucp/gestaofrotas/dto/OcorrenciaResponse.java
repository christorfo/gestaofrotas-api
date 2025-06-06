package com.frotahucp.gestaofrotas.dto;

import com.frotahucp.gestaofrotas.model.Ocorrencia;
import com.frotahucp.gestaofrotas.model.StatusOcorrencia;
import java.time.LocalDateTime;

public class OcorrenciaResponse {
    private Long id;
    private Long veiculoId;
    private String veiculoPlaca;
    private Long motoristaId;
    private String motoristaNome;
    private String descricao;
    private LocalDateTime dataHoraRegistro;
    private StatusOcorrencia status;

    public OcorrenciaResponse(Ocorrencia ocorrencia) {
        this.id = ocorrencia.getId();
        this.veiculoId = ocorrencia.getVeiculo().getId();
        this.veiculoPlaca = ocorrencia.getVeiculo().getPlaca();
        this.motoristaId = ocorrencia.getMotorista().getId();
        this.motoristaNome = ocorrencia.getMotorista().getNomeCompleto();
        this.descricao = ocorrencia.getDescricao();
        this.dataHoraRegistro = ocorrencia.getDataHoraRegistro();
        this.status = ocorrencia.getStatus();
    }
    // Getters
    public Long getId() { return id; }
    public Long getVeiculoId() { return veiculoId; }
    public String getVeiculoPlaca() { return veiculoPlaca; }
    public Long getMotoristaId() { return motoristaId; }
    public String getMotoristaNome() { return motoristaNome; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataHoraRegistro() { return dataHoraRegistro; }
    public StatusOcorrencia getStatus() { return status; }
}