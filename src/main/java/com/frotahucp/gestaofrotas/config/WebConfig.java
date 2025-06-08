package com.frotahucp.gestaofrotas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica a configuração de CORS para todos os endpoints que começam com /api/
                .allowedOrigins("http://localhost:4200") // Permite requisições vindas da origem do seu app Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Especifica os métodos HTTP permitidos
                .allowedHeaders("*") // Permite todos os cabeçalhos na requisição
                .allowCredentials(false); // Se a autenticação não usa cookies de sessão (nosso caso com JWT no header), pode ser false.
    }
}