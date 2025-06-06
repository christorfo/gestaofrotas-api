package com.frotahucp.gestaofrotas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ocorrencias")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id", nullable = false)
    private Motorista motorista; // Motorista que registrou a ocorrência

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_hora_registro", nullable = false)
    private LocalDateTime dataHoraRegistro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOcorrencia status;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
    public Motorista getMotorista() { return motorista; }
    public void setMotorista(Motorista motorista) { this.motorista = motorista; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataHoraRegistro() { return dataHoraRegistro; }
    public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) { this.dataHoraRegistro = dataHoraRegistro; }
    public StatusOcorrencia getStatus() { return status; }
    public void setStatus(StatusOcorrencia status) { this.status = status; }
}