package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.dto.JwtResponse;
import com.frotahucp.gestaofrotas.dto.LoginRequest;
import com.frotahucp.gestaofrotas.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.frotahucp.gestaofrotas.model.Administrador;
import com.frotahucp.gestaofrotas.model.Motorista;
import com.frotahucp.gestaofrotas.repository.AdministradorRepository;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwt = jwtUtil.generateToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String nome = roles.contains("ROLE_ADMINISTRADOR")
                    ? administradorRepository.findByEmail(userDetails.getUsername()).map(Administrador::getNome)
                            .orElse("Admin")
                    : motoristaRepository.findByEmail(userDetails.getUsername()).map(Motorista::getNomeCompleto)
                            .orElse("Motorista");

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), nome, roles));

        } catch (BadCredentialsException e) {
            // Retorna 401 Unauthorized se as credenciais forem inválidas
            return ResponseEntity.status(401).body("Erro: E-mail ou senha inválidos.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro: Ocorreu um erro durante a autenticação.");
        }
    }
}