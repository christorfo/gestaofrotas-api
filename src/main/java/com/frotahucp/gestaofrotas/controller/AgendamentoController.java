package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.model.Agendamento;
import com.frotahucp.gestaofrotas.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.frotahucp.gestaofrotas.dto.IniciarViagemRequest; 
import com.frotahucp.gestaofrotas.dto.FinalizarViagemRequest; 
import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.web.bind.annotation.PathVariable; 

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

        @PostMapping("/{id}/iniciar")
    @PreAuthorize("hasRole('MOTORISTA')")
    public ResponseEntity<?> iniciarViagem(@PathVariable Long id, 
                                           @RequestBody IniciarViagemRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Agendamento agendamentoAtualizado = agendamentoService.iniciarViagem(id, request, userDetails);
            return ResponseEntity.ok(agendamentoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('MOTORISTA')")
    public ResponseEntity<?> finalizarViagem(@PathVariable Long id, 
                                             @RequestBody FinalizarViagemRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Agendamento agendamentoAtualizado = agendamentoService.finalizarViagem(id, request, userDetails);
            return ResponseEntity.ok(agendamentoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}