package com.frotahucp.gestaofrotas.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    // --- Campos (Fields) ---

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Dados do Agendamento Inicial (RF009) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id", nullable = false)
    private Motorista motorista;

    @OneToMany(mappedBy = "agendamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dataHoraAlteracao ASC")
    private List<HistoricoStatusAgendamento> historicoStatus = new ArrayList<>();

    @Column(name = "data_hora_saida", nullable = false)
    private LocalDateTime dataHoraSaida;

    @Column(nullable = false)
    private String destino;

    @Column(columnDefinition = "TEXT")
    private String justificativa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;

    // --- Dados do Início da Viagem (RF005) ---
    @Column(name = "data_hora_inicio_viagem")
    private LocalDateTime dataHoraInicioViagem;

    @Column(name = "quilometragem_saida")
    private Integer quilometragemSaida;

    @Column(name = "observacoes_saida", columnDefinition = "TEXT")
    private String observacoesSaida;

    // --- Dados do Fim da Viagem (RF006) ---
    @Column(name = "data_hora_retorno")
    private LocalDateTime dataHoraRetorno;

    @Column(name = "quilometragem_final")
    private Integer quilometragemFinal;

    @Column(name = "observacoes_retorno", columnDefinition = "TEXT")
    private String observacoesRetorno;

    // --- Construtor Padrão ---
    public Agendamento() {
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public LocalDateTime getDataHoraInicioViagem() {
        return dataHoraInicioViagem;
    }

    public void setDataHoraInicioViagem(LocalDateTime dataHoraInicioViagem) {
        this.dataHoraInicioViagem = dataHoraInicioViagem;
    }

    public Integer getQuilometragemSaida() {
        return quilometragemSaida;
    }

    public void setQuilometragemSaida(Integer quilometragemSaida) {
        this.quilometragemSaida = quilometragemSaida;
    }

    public String getObservacoesSaida() {
        return observacoesSaida;
    }

    public void setObservacoesSaida(String observacoesSaida) {
        this.observacoesSaida = observacoesSaida;
    }

    public LocalDateTime getDataHoraRetorno() {
        return dataHoraRetorno;
    }

    public void setDataHoraRetorno(LocalDateTime dataHoraRetorno) {
        this.dataHoraRetorno = dataHoraRetorno;
    }

    public Integer getQuilometragemFinal() {
        return quilometragemFinal;
    }

    public void setQuilometragemFinal(Integer quilometragemFinal) {
        this.quilometragemFinal = quilometragemFinal;
    }

    public String getObservacoesRetorno() {
        return observacoesRetorno;
    }

    public void setObservacoesRetorno(String observacoesRetorno) {
        this.observacoesRetorno = observacoesRetorno;
    }

    public List<HistoricoStatusAgendamento> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<HistoricoStatusAgendamento> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }

}