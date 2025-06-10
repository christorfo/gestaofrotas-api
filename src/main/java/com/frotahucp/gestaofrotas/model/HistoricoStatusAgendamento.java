package com.frotahucp.gestaofrotas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status_agendamento")
public class HistoricoStatusAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior")
    private StatusAgendamento statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private StatusAgendamento statusNovo;

    @Column(name = "data_hora_alteracao", nullable = false)
    private LocalDateTime dataHoraAlteracao;

    @Column(name = "usuario_responsavel", nullable = false)
    private String usuarioResponsavel;

    // --- Construtores ---
    public HistoricoStatusAgendamento() {
    }

    public HistoricoStatusAgendamento(Agendamento agendamento, StatusAgendamento statusAnterior,
            StatusAgendamento statusNovo, String usuarioResponsavel) {
        this.agendamento = agendamento;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.dataHoraAlteracao = LocalDateTime.now();
        this.usuarioResponsavel = usuarioResponsavel;
    }

    // --- Getters e Setters Completos ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public StatusAgendamento getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusAgendamento statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusAgendamento getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusAgendamento statusNovo) {
        this.statusNovo = statusNovo;
    }

    public LocalDateTime getDataHoraAlteracao() {
        return dataHoraAlteracao;
    }

    public void setDataHoraAlteracao(LocalDateTime dataHoraAlteracao) {
        this.dataHoraAlteracao = dataHoraAlteracao;
    }

    public String getUsuarioResponsavel() {
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(String usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }
}