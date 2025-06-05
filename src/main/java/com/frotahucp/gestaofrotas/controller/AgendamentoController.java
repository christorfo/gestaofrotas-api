package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.model.Agendamento;
import com.frotahucp.gestaofrotas.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')") // Apenas usuários com o papel 'ROLE_ADMINISTRADOR' podem acessar
    public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoRequest request) {
        try {
            Agendamento novoAgendamento = agendamentoService.criarAgendamento(request);
            return new ResponseEntity<>(novoAgendamento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}