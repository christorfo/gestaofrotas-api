package com.frotahucp.gestaofrotas.config;

import com.frotahucp.gestaofrotas.security.JwtRequestFilter; // Importar o filtro
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Importar para stateless
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Importar para adicionar filtro antes
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // Injetar nosso filtro

    @Bean
    public PasswordEncoder passwordEncoder() {
        // PasswordEncoderFactories.createDelegatingPasswordEncoder()
        // por padrão, para novas senhas, usa BCrypt.
        // Se quisermos ser mais próximos do SHA-256, podemos especificar Pbkdf2PasswordEncoder,
        // que, em suas versões mais recentes (ex: Spring Security 5.8+),
        // usa PBKDF2WithHmacSHA256.
        // Para usar o default do Spring Security 6 (que é BCrypt e é muito seguro):
        // return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // Para explicitamente usar PBKDF2 (que usa HMAC SHA256 por padrão nas versões recentes)
        // e estar alinhado com a ideia de "SHA-256 + SALT":
        // Este é um dos encoders que o DelegatingPasswordEncoder pode usar se a senha estiver prefixada com {pbkdf2}.
        // Para consistência e para que novas senhas sejam codificadas com ele por padrão via DelegatingPasswordEncoder,
        // a configuração do DelegatingPasswordEncoder em si é mais complexa.
        // Por simplicidade e segurança robusta, BCrypt (o default do DelegatingPasswordEncoder) é excelente.

        // Vamos usar o DelegatingPasswordEncoder, que é a prática recomendada.
        // Ele suporta múltiplos algoritmos de codificação e prefixa o hash da senha
        // com o ID do algoritmo usado (ex: {bcrypt}, {pbkdf2}).
        // Por padrão, para novas codificações, ele usará BCrypt.
        // BCrypt é altamente recomendado e lida com salting automaticamente.
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 1. Torna a API stateless
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/api/auth/**").permitAll() // 2. Permite acesso aos endpoints de autenticação
                    .requestMatchers("/h2-console/**").permitAll() // Permite acesso ao console H2
                    .anyRequest().authenticated() // 3. Todas as outras requisições exigem autenticação
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // Para o console H2

        // 4. Adiciona o filtro JWT antes do filtro padrão de autenticação de username/password
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}