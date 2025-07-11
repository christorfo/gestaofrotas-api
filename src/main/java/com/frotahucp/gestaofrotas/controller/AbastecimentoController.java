package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.dto.AbastecimentoRequest;
import com.frotahucp.gestaofrotas.dto.AbastecimentoResponse;
import com.frotahucp.gestaofrotas.service.AbastecimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/abastecimentos")
public class AbastecimentoController {

    @Autowired
    private AbastecimentoService abastecimentoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> registrarAbastecimento(@RequestBody AbastecimentoRequest request) {
        try {
            AbastecimentoResponse response = abastecimentoService.registrarAbastecimento(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}