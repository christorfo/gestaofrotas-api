package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.dto.OcorrenciaRequest;
import com.frotahucp.gestaofrotas.dto.OcorrenciaResponse;
import com.frotahucp.gestaofrotas.service.OcorrenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaController {

    @Autowired
    private OcorrenciaService ocorrenciaService;

    @PostMapping
    @PreAuthorize("hasRole('MOTORISTA')")
    public ResponseEntity<?> registrarOcorrencia(@RequestBody OcorrenciaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            OcorrenciaResponse response = ocorrenciaService.registrarOcorrencia(request, userDetails);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<OcorrenciaResponse>> listarOcorrencias() {
        List<OcorrenciaResponse> response = ocorrenciaService.listarTodasOcorrencias();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/resolver")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> resolverOcorrencia(@PathVariable Long id) {
        try {
            OcorrenciaResponse response = ocorrenciaService.resolverOcorrencia(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}