package com.frotahucp.gestaofrotas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint; // Para constraints de unicidade compostas, se necessário

import java.time.LocalDate;

@Entity
@Table(name = "motoristas", uniqueConstraints = {
    @UniqueConstraint(columnNames = "cpf"),
    @UniqueConstraint(columnNames = "email")
})
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto; // [cite: 25]

    @Column(nullable = false, length = 14) // Formato XXX.XXX.XXX-XX para CPF
    private String cpf; // [cite: 25]

    @Column(name = "cnh_numero", nullable = false, length = 20)
    private String cnhNumero; // Parte de "CNH e validade" [cite: 25]

    @Column(name = "cnh_validade", nullable = false)
    private LocalDate cnhValidade; // Parte de "CNH e validade" [cite: 25]

    @Column(nullable = false, length = 20)
    private String telefone; // [cite: 25]

    @Column(length = 9) // Formato XXXXX-XXX
    private String cep; // Novo campo para o CEP

    @Column(length = 255) // Para o endereço completo
    private String endereco; // [cite: 25] (ViaCEP será tratado depois)

    @Column(nullable = false, length = 100)
    private String email; // [cite: 25] (Usado para login)

    @Column(nullable = false, length = 76) // Comprimento típico para hash BCrypt
    private String senha; // [cite: 25] (Armazenará o hash)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatusUsuario status = StatusUsuario.ATIVO; // Motoristas são ATIVOS por padrão ao cadastrar

    // Construtores
    public Motorista() {
    }

    // Getters e Setters (essenciais para JPA e para a lógica de serviço/controlador)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnhNumero() {
        return cnhNumero;
    }

    public void setCnhNumero(String cnhNumero) {
        this.cnhNumero = cnhNumero;
    }

    public LocalDate getCnhValidade() {
        return cnhValidade;
    }

    public void setCnhValidade(LocalDate cnhValidade) {
        this.cnhValidade = cnhValidade;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    // (Opcional) toString, equals, hashCode
}