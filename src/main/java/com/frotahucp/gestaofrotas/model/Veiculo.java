package com.frotahucp.gestaofrotas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "veiculos") // Define o nome da tabela no banco de dados
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10) // Placa é obrigatória, única e tem um tamanho máximo
    private String placa;

    @Column(nullable = false, length = 50) // Modelo é obrigatório
    private String modelo;

    @Column(nullable = false, length = 30) // Tipo é obrigatório (ex: carro, van, caminhão)
    private String tipo;

    @Column(nullable = false) // Ano é obrigatório
    private int ano;

    @Column(name = "quilometragem_atual", nullable = false) // Quilometragem é obrigatória
    private int quilometragemAtual;

    @Enumerated(EnumType.STRING) // Define como o Enum será persistido (como String)
    @Column(nullable = false, length = 20) // Status é obrigatório
    private StatusVeiculo status;

    // Construtores

    public Veiculo() {
        // Construtor padrão exigido pelo JPA
    }

    public Veiculo(String placa, String modelo, String tipo, int ano, int quilometragemAtual, StatusVeiculo status) {
        this.placa = placa;
        this.modelo = modelo;
        this.tipo = tipo;
        this.ano = ano;
        this.quilometragemAtual = quilometragemAtual;
        this.status = status;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getQuilometragemAtual() {
        return quilometragemAtual;
    }

    public void setQuilometragemAtual(int quilometragemAtual) {
        this.quilometragemAtual = quilometragemAtual;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public void setStatus(StatusVeiculo status) {
        this.status = status;
    }

    // toString (opcional, mas útil para debugging)
    @Override
    public String toString() {
        return "Veiculo{" +
               "id=" + id +
               ", placa='" + placa + '\'' +
               ", modelo='" + modelo + '\'' +
               ", tipo='" + tipo + '\'' +
               ", ano=" + ano +
               ", quilometragemAtual=" + quilometragemAtual +
               ", status=" + status +
               '}';
    }
}