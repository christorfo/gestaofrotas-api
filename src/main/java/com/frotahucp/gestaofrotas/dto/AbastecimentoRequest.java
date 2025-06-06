package com.frotahucp.gestaofrotas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbastecimentoRequest {
    private Long veiculoId;
    private LocalDate data;
    private String tipoCombustivel;
    private BigDecimal valor;
    private int quilometragem;
    private String motoristaResponsavel;

    // Getters e Setters
    public Long getVeiculoId() { return veiculoId; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getTipoCombustivel() { return tipoCombustivel; }
    public void setTipoCombustivel(String tipoCombustivel) { this.tipoCombustivel = tipoCombustivel; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public int getQuilometragem() { return quilometragem; }
    public void setQuilometragem(int quilometragem) { this.quilometragem = quilometragem; }
    public String getMotoristaResponsavel() { return motoristaResponsavel; }
    public void setMotoristaResponsavel(String motoristaResponsavel) { this.motoristaResponsavel = motoristaResponsavel; }
}