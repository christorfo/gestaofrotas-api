package com.frotahucp.gestaofrotas.config;

import com.frotahucp.gestaofrotas.model.Administrador;
import com.frotahucp.gestaofrotas.repository.AdministradorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe algum administrador
        if (administradorRepository.count() == 0) {
            logger.info("Nenhum administrador encontrado. Criando usuário admin padrão...");
            
            Administrador admin = new Administrador();
            admin.setNome("Admin Padrão");
            admin.setEmail("admin@frotahucp.com");
            admin.setSenha(passwordEncoder.encode("admin123")); // Senha padrão (será hasheada)
            
            administradorRepository.save(admin);
            
            logger.info("Usuário 'admin@frotahucp.com' criado com sucesso. Senha: 'admin123'");
        } else {
            logger.info("Banco de dados já contém administradores.");
        }
    }
}