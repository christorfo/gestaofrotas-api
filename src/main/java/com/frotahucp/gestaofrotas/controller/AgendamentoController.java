package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.dto.AgendamentoRequest;
import com.frotahucp.gestaofrotas.dto.AgendamentoResponse;
import com.frotahucp.gestaofrotas.dto.FinalizarViagemRequest;
import com.frotahucp.gestaofrotas.dto.IniciarViagemRequest;
import com.frotahucp.gestaofrotas.service.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.frotahucp.gestaofrotas.model.StatusAgendamento;

import org.springframework.security.core.annotation.AuthenticationPrincipal; 
import org.springframework.security.core.userdetails.UserDetails; 

import org.springframework.format.annotation.DateTimeFormat; 
import java.time.LocalDate; 
import java.util.List; 

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoRequest request) {
        try {
            AgendamentoResponse response = agendamentoService.criarAgendamento(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
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
            AgendamentoResponse response = agendamentoService.iniciarViagem(id, request, userDetails);
            return ResponseEntity.ok(response);
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
            AgendamentoResponse response = agendamentoService.finalizarViagem(id, request, userDetails);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<AgendamentoResponse>> listarAgendamentos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) Long motoristaId,
            @RequestParam(required = false) StatusAgendamento status) {
        
        List<AgendamentoResponse> agendamentos = agendamentoService.listarAgendamentosFiltrados(
                dataInicio, dataFim, motoristaId, status
        );
        
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/meus-agendamentos")
    @PreAuthorize("hasRole('MOTORISTA')")
    public ResponseEntity<List<AgendamentoResponse>> listarMeusAgendamentos(@AuthenticationPrincipal UserDetails userDetails) {
        List<AgendamentoResponse> agendamentos = agendamentoService.listarAgendamentosDoMotorista(userDetails);
        return ResponseEntity.ok(agendamentos);
    }
}   