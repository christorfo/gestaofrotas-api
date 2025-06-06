package com.frotahucp.gestaofrotas.dto;

import com.frotahucp.gestaofrotas.model.TipoManutencao;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ManutencaoRequest {
    private Long veiculoId;
    private LocalDate data;
    private TipoManutencao tipo;
    private String descricao;
    private BigDecimal valor;
    private int quilometragem;

    // Getters e Setters
    public Long getVeiculoId() { return veiculoId; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public TipoManutencao getTipo() { return tipo; }
    public void setTipo(TipoManutencao tipo) { this.tipo = tipo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public int getQuilometragem() { return quilometragem; }
    public void setQuilometragem(int quilometragem) { this.quilometragem = quilometragem; }
}