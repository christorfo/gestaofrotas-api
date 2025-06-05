package com.frotahucp.gestaofrotas.model;

public enum StatusVeiculo {
    DISPONIVEL("Disponível"),
    INATIVO("Inativo"),
    EM_MANUTENCAO("Em Manutenção");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}