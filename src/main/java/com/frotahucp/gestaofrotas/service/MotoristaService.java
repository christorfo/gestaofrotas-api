package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.dto.ViaCepResponseDto;
import com.frotahucp.gestaofrotas.model.Motorista;
import com.frotahucp.gestaofrotas.model.StatusUsuario;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
// import com.frotahucp.gestaofrotas.exception.ViaCepException; // Se for usar a exceção customizada
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class MotoristaService {

    private static final Logger logger = LoggerFactory.getLogger(MotoristaService.class);

    private final MotoristaRepository motoristaRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient webClient;

    @Autowired
    public MotoristaService(MotoristaRepository motoristaRepository,
                            PasswordEncoder passwordEncoder,
                            WebClient.Builder webClientBuilder) {
        this.motoristaRepository = motoristaRepository;
        this.passwordEncoder = passwordEncoder;
        this.webClient = webClientBuilder.baseUrl("https://viacep.com.br/ws").build();
    }

    private String consultarEnderecoPorCep(String cep) {
        if (!StringUtils.hasText(cep)) {
            return null; // Nenhum CEP fornecido, nada a fazer.
        }
        String cepNumerico = cep.replaceAll("[^0-9]", "");
        if (cepNumerico.length() != 8) {
            logger.warn("Formato de CEP inválido fornecido: {}", cep);
            throw new RuntimeException("Formato de CEP inválido: " + cep); // Lançar exceção
        }

        try {
            logger.info("Consultando ViaCEP para o CEP: {}", cepNumerico);
            ViaCepResponseDto responseDto = this.webClient.get()
                    .uri("/{cep}/json", cepNumerico)
                    .retrieve()
                    .bodyToMono(ViaCepResponseDto.class)
                    .block(); // Chamada bloqueante

            if (responseDto == null) {
                logger.error("Resposta nula do ViaCEP para o CEP: {}", cepNumerico);
                throw new RuntimeException("Erro ao consultar o ViaCEP (resposta nula) para o CEP: " + cep);
            }
            if (responseDto.isErro()) {
                logger.warn("ViaCEP retornou erro para o CEP (CEP não encontrado ou inexistente): {}", cepNumerico);
                throw new RuntimeException("CEP não encontrado ou inexistente na base do ViaCEP: " + cep);
            }

            String enderecoFormatado = responseDto.getEnderecoFormatado();
            if (!StringUtils.hasText(enderecoFormatado)) { // Checagem extra, embora isErro() deva cobrir
                logger.warn("Endereço formatado vazio mesmo sem erro explícito do ViaCEP para CEP: {}", cepNumerico);
                throw new RuntimeException("Não foi possível obter um endereço válido para o CEP: " + cep);
            }
            logger.info("Endereço obtido para o CEP {}: {}", cepNumerico, enderecoFormatado);
            return enderecoFormatado;

        } catch (RuntimeException e) { // Captura as exceções lançadas acima ou do WebClient
            logger.error("Exceção ao consultar ViaCEP para o CEP {}: {}", cepNumerico, e.getMessage());
            throw e; // Re-lança para ser tratada pelo controller
        } catch (Exception e) { // Captura outras exceções inesperadas (ex: problemas de rede com WebClient)
            logger.error("Erro inesperado ao consultar ViaCEP para o CEP {}: {}", cepNumerico, e.getMessage(), e);
            throw new RuntimeException("Erro de comunicação ao tentar consultar o CEP: " + cep, e);
        }
    }

    @Transactional
    public Motorista cadastrarMotorista(Motorista motorista) {
        if (motoristaRepository.findByCpf(motorista.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado: " + motorista.getCpf());
        }
        if (motoristaRepository.findByEmail(motorista.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado: " + motorista.getEmail());
        }

        if (StringUtils.hasText(motorista.getCep())) {
            String enderecoConsultado = consultarEnderecoPorCep(motorista.getCep());
            // A exceção de consultarEnderecoPorCep já interrompe se o endereço não for encontrado
            motorista.setEndereco(enderecoConsultado);
        } else {
            // Se nenhum CEP for fornecido, o endereço pode ser nulo ou preenchido manualmente (se permitido pela UI)
             motorista.setEndereco(motorista.getEndereco()); // Mantém o que veio, pode ser nulo ou preenchido manualmente
        }

        motorista.setSenha(passwordEncoder.encode(motorista.getSenha()));
        motorista.setStatus(StatusUsuario.ATIVO);
        return motoristaRepository.save(motorista);
    }

    @Transactional
    public Motorista editarMotorista(Long id, Motorista motoristaDetalhes) {
        Motorista motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + id));

        // Validações de unicidade para CPF e Email se forem alterados e diferentes do original
        if (!motoristaExistente.getCpf().equals(motoristaDetalhes.getCpf()) &&
            motoristaRepository.findByCpf(motoristaDetalhes.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado para outro motorista: " + motoristaDetalhes.getCpf());
        }
        if (!motoristaExistente.getEmail().equals(motoristaDetalhes.getEmail()) &&
            motoristaRepository.findByEmail(motoristaDetalhes.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado para outro motorista: " + motoristaDetalhes.getEmail());
        }

        // Atualiza todos os campos da entidade existente com os detalhes fornecidos
        motoristaExistente.setNomeCompleto(motoristaDetalhes.getNomeCompleto());
        motoristaExistente.setCpf(motoristaDetalhes.getCpf());
        motoristaExistente.setCnhNumero(motoristaDetalhes.getCnhNumero());
        motoristaExistente.setCnhValidade(motoristaDetalhes.getCnhValidade());
        motoristaExistente.setTelefone(motoristaDetalhes.getTelefone());
        motoristaExistente.setEmail(motoristaDetalhes.getEmail());
        motoristaExistente.setCep(motoristaDetalhes.getCep()); // Atualiza o CEP

        if (StringUtils.hasText(motoristaDetalhes.getCep())) {
            String enderecoConsultado = consultarEnderecoPorCep(motoristaDetalhes.getCep());
            // A exceção de consultarEnderecoPorCep já interrompe se o endereço não for encontrado
            motoristaExistente.setEndereco(enderecoConsultado);
        } else {
            // Se o CEP for removido/esvaziado nos detalhes, limpar o endereço existente.
            motoristaExistente.setEndereco(null);
        }

        if (StringUtils.hasText(motoristaDetalhes.getSenha())) {
            // Apenas atualiza a senha se uma nova senha (não vazia) for fornecida
            motoristaExistente.setSenha(passwordEncoder.encode(motoristaDetalhes.getSenha()));
        }

        if (motoristaDetalhes.getStatus() != null) {
            motoristaExistente.setStatus(motoristaDetalhes.getStatus());
        }

        return motoristaRepository.save(motoristaExistente);
    }

    // --- Métodos restantes permanecem os mesmos ---
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
    public Motorista inativarMotorista(Long id) {
        Motorista motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + id));
        motoristaExistente.setStatus(StatusUsuario.INATIVO);
        return motoristaRepository.save(motoristaExistente);
    }

    @Transactional
    public Motorista ativarMotorista(Long id) {
        Motorista motoristaExistente = motoristaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motorista não encontrado com o ID: " + id));
        motoristaExistente.setStatus(StatusUsuario.ATIVO);
        return motoristaRepository.save(motoristaExistente);
    }
}