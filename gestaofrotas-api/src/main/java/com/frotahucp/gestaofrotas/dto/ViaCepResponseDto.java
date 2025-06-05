package com.frotahucp.gestaofrotas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// Ignora propriedades desconhecidas que podem vir do JSON para não quebrar o parsing
@JsonIgnoreProperties(ignoreUnknown = true)
public class ViaCepResponseDto {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade; // Cidade
    private String uf; // Estado
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;
    private boolean erro; // Campo que indica se houve erro na consulta do CEP

    // Getters e Setters para todos os campos

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getLogradouro() { return logradouro; }
    public void setLogradouro(String logradouro) { this.logradouro = logradouro; }
    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getLocalidade() { return localidade; }
    public void setLocalidade(String localidade) { this.localidade = localidade; }
    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }
    public String getIbge() { return ibge; }
    public void setIbge(String ibge) { this.ibge = ibge; }
    public String getGia() { return gia; }
    public void setGia(String gia) { this.gia = gia; }
    public String getDdd() { return ddd; }
    public void setDdd(String ddd) { this.ddd = ddd; }
    public String getSiafi() { return siafi; }
    public void setSiafi(String siafi) { this.siafi = siafi; }
    public boolean isErro() { return erro; }
    public void setErro(boolean erro) { this.erro = erro; }

    // Método para formatar o endereço completo
    public String getEnderecoFormatado() {
        if (erro || logradouro == null) {
            return null; // Ou lançar uma exceção / retornar uma mensagem de erro
        }
        StringBuilder sb = new StringBuilder();
        sb.append(logradouro);
        if (complemento != null && !complemento.isEmpty()) {
            sb.append(", ").append(complemento);
        }
        if (bairro != null && !bairro.isEmpty()) {
            sb.append(" - ").append(bairro);
        }
        sb.append(", ").append(localidade).append("/").append(uf);
        // sb.append(" - CEP: ").append(cep); // Opcional incluir o CEP no endereço formatado
        return sb.toString();
    }
}