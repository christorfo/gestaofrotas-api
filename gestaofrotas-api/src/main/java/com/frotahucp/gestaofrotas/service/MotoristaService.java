package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.model.Motorista;
import com.frotahucp.gestaofrotas.model.StatusUsuario;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // Para verificar se a string de senha é vazia

import java.util.List;
import java.util.Optional;

@Service
public class MotoristaService {

    private final MotoristaRepository motoristaRepository;
    private final PasswordEncoder passwordEncoder; // Injetar o PasswordEncoder

    @Autowired
    public MotoristaService(MotoristaRepository motoristaRepository, PasswordEncoder passwordEncoder) {
        this.motoristaRepository = motoristaRepository;
        this.passwordEncoder = passwordEncoder; // Adicionar ao construtor
    }

    @Transactional
    public Motorista cadastrarMotorista(Motorista motorista) {
        // Verificar se CPF ou Email já existem (opcional, pois o BD já tem constraints)
        if (motoristaRepository.findByCpf(motorista.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado: " + motorista.getCpf()); // Usar exceções mais específicas
        }
        if (motoristaRepository.findByEmail(motorista.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado: " + motorista.getEmail()); // Usar exceções mais específicas
        }

        // Codificar a senha antes de salvar
        motorista.setSenha(passwordEncoder.encode(motorista.getSenha()));
        motorista.setStatus(StatusUsuario.ATIVO); // Garante que o motorista seja cadastrado como ATIVO
        return motoristaRepository.save(motorista);
    }

    @Transactional(readOnly = true)
    public List<Motorista> listarTodosMotoristas() {
        return motoristaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Motorista> buscarMotoristaPorId(Long id) {
        return motoristaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Motorista> buscarMotoristaPorEmail(String email) {
        return motoristaRepository.findByEmail(email);
    }


    @Transactional
    public Motorista editarMotorista(Long id, Motorista motoristaDetalhes) {
        Motorista motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + id)); // Usar exceção mais específica

        // Atualiza os campos básicos
        motoristaExistente.setNomeCompleto(motoristaDetalhes.getNomeCompleto());
        motoristaExistente.setCpf(motoristaDetalhes.getCpf()); // Considerar validação de unicidade se o CPF for alterado
        motoristaExistente.setCnhNumero(motoristaDetalhes.getCnhNumero());
        motoristaExistente.setCnhValidade(motoristaDetalhes.getCnhValidade());
        motoristaExistente.setTelefone(motoristaDetalhes.getTelefone());
        motoristaExistente.setEndereco(motoristaDetalhes.getEndereco()); // A lógica do ViaCEP entraria aqui ou antes
        motoristaExistente.setEmail(motoristaDetalhes.getEmail()); // Considerar validação de unicidade se o e-mail for alterado

        // Se uma nova senha for fornecida (não nula e não vazia), codifica e atualiza
        if (StringUtils.hasText(motoristaDetalhes.getSenha())) {
            // Poderia adicionar uma verificação aqui para não sobrescrever com um hash de string vazia,
            // ou garantir que a senha no DTO/request não seja o hash atual.
            // A forma mais simples é: se veio algo no campo senha, considera-se que é uma nova senha.
            motoristaExistente.setSenha(passwordEncoder.encode(motoristaDetalhes.getSenha()));
        }

        // Atualiza o status se fornecido (opcional, pode ter um método dedicado para status)
        if (motoristaDetalhes.getStatus() != null) {
            motoristaExistente.setStatus(motoristaDetalhes.getStatus());
        }

        return motoristaRepository.save(motoristaExistente);
    }

    @Transactional
    public Motorista inativarMotorista(Long id) {
        Motorista motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + id)); // Usar exceção mais específica

        motoristaExistente.setStatus(StatusUsuario.INATIVO);
        return motoristaRepository.save(motoristaExistente);
    }

    @Transactional
    public Motorista ativarMotorista(Long id) {
        Motorista motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + id)); // Usar exceção mais específica

        motoristaExistente.setStatus(StatusUsuario.ATIVO);
        return motoristaRepository.save(motoristaExistente);
    }
}