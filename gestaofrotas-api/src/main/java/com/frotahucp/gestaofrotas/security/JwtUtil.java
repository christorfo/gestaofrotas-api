package com.frotahucp.gestaofrotas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // ATENÇÃO: Em um ambiente de produção, esta chave secreta DEVE ser muito mais complexa
    // e armazenada de forma segura, por exemplo, em variáveis de ambiente ou um cofre de segredos.
    // NÃO use chaves fracas ou fixas no código em produção.
    // Para este exemplo, geraremos uma chave segura na inicialização.
    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Gera uma chave segura para HS256

    // Tempo de expiração do token (ex: 10 horas)
    private final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 10; // 10 horas em milissegundos

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(SECRET_KEY)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Você pode adicionar claims personalizadas aqui, como papéis (roles)
        String roles = userDetails.getAuthorities().stream()
                           .map(auth -> auth.getAuthority())
                           .collect(Collectors.joining(","));
        claims.put("roles", roles);

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .claims(claims)
                   .subject(subject) // O "subject" do token, geralmente o username (email no nosso caso)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                   .signWith(SECRET_KEY)
                   .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}