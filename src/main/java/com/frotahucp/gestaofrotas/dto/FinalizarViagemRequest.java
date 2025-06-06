package com.frotahucp.gestaofrotas.dto;

public class FinalizarViagemRequest {
    private int quilometragemFinal;
    private String observacoesRetorno;

    // Getters e Setters
    public int getQuilometragemFinal() { return quilometragemFinal; }
    public void setQuilometragemFinal(int quilometragemFinal) { this.quilometragemFinal = quilometragemFinal; }
    public String getObservacoesRetorno() { return observacoesRetorno; }
    public void setObservacoesRetorno(String observacoesRetorno) { this.observacoesRetorno = observacoesRetorno; }
}