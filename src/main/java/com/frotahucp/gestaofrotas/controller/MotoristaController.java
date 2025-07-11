package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.model.Motorista;
import com.frotahucp.gestaofrotas.service.MotoristaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {

    private final MotoristaService motoristaService;

    @Autowired
    public MotoristaController(MotoristaService motoristaService) {
        this.motoristaService = motoristaService;
    }

    // Endpoint para CADASTRAR um novo motorista
    @PostMapping
    public ResponseEntity<?> cadastrarMotorista(@RequestBody Motorista motorista) {
        try {
            Motorista novoMotorista = motoristaService.cadastrarMotorista(motorista);
            return new ResponseEntity<>(novoMotorista, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Se o serviço lançar exceção por CPF/Email duplicado, por exemplo
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para LISTAR todos os motoristas
    @GetMapping
    public ResponseEntity<List<Motorista>> listarTodosMotoristas() {
        List<Motorista> motoristas = motoristaService.listarTodosMotoristas();
        return ResponseEntity.ok(motoristas);
    }

    // Endpoint para BUSCAR um motorista por ID
    @GetMapping("/{id}")
    public ResponseEntity<Motorista> buscarMotoristaPorId(@PathVariable Long id) {
        Optional<Motorista> motoristaOptional = motoristaService.buscarMotoristaPorId(id);
        return motoristaOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para EDITAR um motorista existente
    @PutMapping("/{id}")
    public ResponseEntity<?> editarMotorista(@PathVariable Long id, @RequestBody Motorista motoristaDetalhes) {
        try {
            Motorista motoristaAtualizado = motoristaService.editarMotorista(id, motoristaDetalhes);
            return ResponseEntity.ok(motoristaAtualizado);
        } catch (RuntimeException e) {
             if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para INATIVAR um motorista
    @PostMapping("/{id}/inativar")
    public ResponseEntity<Motorista> inativarMotorista(@PathVariable Long id) {
        try {
            Motorista motoristaInativado = motoristaService.inativarMotorista(id);
            return ResponseEntity.ok(motoristaInativado);
        } catch (RuntimeException e) { // Ex: Motorista não encontrado
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para ATIVAR um motorista
    @PostMapping("/{id}/ativar")
    public ResponseEntity<Motorista> ativarMotorista(@PathVariable Long id) {
        try {
            Motorista motoristaAtivado = motoristaService.ativarMotorista(id);
            return ResponseEntity.ok(motoristaAtivado);
        } catch (RuntimeException e) { // Ex: Motorista não encontrado
            return ResponseEntity.notFound().build();
        }
    }
}