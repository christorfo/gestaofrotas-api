package com.frotahucp.gestaofrotas.dto;

public class IniciarViagemRequest {
    private int quilometragemSaida;
    private String observacoesSaida;

    // Getters e Setters
    public int getQuilometragemSaida() { return quilometragemSaida; }
    public void setQuilometragemSaida(int quilometragemSaida) { this.quilometragemSaida = quilometragemSaida; }
    public String getObservacoesSaida() { return observacoesSaida; }
    public void setObservacoesSaida(String observacoesSaida) { this.observacoesSaida = observacoesSaida; }
}