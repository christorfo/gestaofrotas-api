package com.frotahucp.gestaofrotas.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "abastecimentos")
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "tipo_combustivel", nullable = false)
    private String tipoCombustivel;

    @Column(nullable = false, precision = 10, scale = 2) // Bom para valores monetários
    private BigDecimal valor;

    @Column(nullable = false)
    private int quilometragem;

    @Column(name = "motorista_responsavel", nullable = false)
    private String motoristaResponsavel;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
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