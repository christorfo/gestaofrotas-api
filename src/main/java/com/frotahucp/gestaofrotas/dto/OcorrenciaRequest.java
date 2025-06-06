package com.frotahucp.gestaofrotas.dto;

public class OcorrenciaRequest {
    private Long veiculoId;
    private String descricao;

    // Getters e Setters
    public Long getVeiculoId() { return veiculoId; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}