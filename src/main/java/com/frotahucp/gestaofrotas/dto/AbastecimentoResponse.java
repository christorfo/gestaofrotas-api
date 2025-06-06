package com.frotahucp.gestaofrotas.dto;

import com.frotahucp.gestaofrotas.model.Abastecimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AbastecimentoResponse {
    private Long id;
    private Long veiculoId;
    private String veiculoPlaca;
    private LocalDate data;
    private String tipoCombustivel;
    private BigDecimal valor;
    private int quilometragem;
    private String motoristaResponsavel;

    public AbastecimentoResponse(Abastecimento abastecimento) {
        this.id = abastecimento.getId();
        this.veiculoId = abastecimento.getVeiculo().getId();
        this.veiculoPlaca = abastecimento.getVeiculo().getPlaca();
        this.data = abastecimento.getData();
        this.tipoCombustivel = abastecimento.getTipoCombustivel();
        this.valor = abastecimento.getValor();
        this.quilometragem = abastecimento.getQuilometragem();
        this.motoristaResponsavel = abastecimento.getMotoristaResponsavel();
    }
    // Getters
    public Long getId() { return id; }
    public Long getVeiculoId() { return veiculoId; }
    public String getVeiculoPlaca() { return veiculoPlaca; }
    public LocalDate getData() { return data; }
    public String getTipoCombustivel() { return tipoCombustivel; }
    public BigDecimal getValor() { return valor; }
    public int getQuilometragem() { return quilometragem; }
    public String getMotoristaResponsavel() { return motoristaResponsavel; }
}