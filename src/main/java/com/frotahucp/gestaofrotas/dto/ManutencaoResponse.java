package com.frotahucp.gestaofrotas.dto;

import com.frotahucp.gestaofrotas.model.Manutencao;
import com.frotahucp.gestaofrotas.model.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ManutencaoResponse {
    private Long id;
    private Long veiculoId;
    private String veiculoPlaca;
    private LocalDate data;
    private TipoManutencao tipo;
    private String descricao;
    private BigDecimal valor;
    private int quilometragem;

    public ManutencaoResponse(Manutencao manutencao) {
        this.id = manutencao.getId();
        this.veiculoId = manutencao.getVeiculo().getId();
        this.veiculoPlaca = manutencao.getVeiculo().getPlaca();
        this.data = manutencao.getData();
        this.tipo = manutencao.getTipo();
        this.descricao = manutencao.getDescricao();
        this.valor = manutencao.getValor();
        this.quilometragem = manutencao.getQuilometragem();
    }
    // Getters
    public Long getId() { return id; }
    public Long getVeiculoId() { return veiculoId; }
    public String getVeiculoPlaca() { return veiculoPlaca; }
    public LocalDate getData() { return data; }
    public TipoManutencao getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public int getQuilometragem() { return quilometragem; }
}