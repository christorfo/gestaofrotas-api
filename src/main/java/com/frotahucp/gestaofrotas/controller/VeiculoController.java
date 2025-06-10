package com.frotahucp.gestaofrotas.controller;

import com.frotahucp.gestaofrotas.model.Veiculo;
import com.frotahucp.gestaofrotas.model.StatusVeiculo; // Necessário se for usar um endpoint para atualizar status genérico
import com.frotahucp.gestaofrotas.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/veiculos") // Define o caminho base para todos os endpoints deste controlador
public class VeiculoController {

    private final VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    // Endpoint para CADASTRAR um novo veículo (RF010)
    @PostMapping
    public ResponseEntity<Veiculo> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        Veiculo novoVeiculo = veiculoService.cadastrarVeiculo(veiculo);
        return new ResponseEntity<>(novoVeiculo, HttpStatus.CREATED);
    }

    // Endpoint para LISTAR todos os veículos (RF010)
    @GetMapping
    public ResponseEntity<List<Veiculo>> listarTodosVeiculos() {
        List<Veiculo> veiculos = veiculoService.listarTodosVeiculos();
        return ResponseEntity.ok(veiculos);
    }

    // Endpoint para BUSCAR um veículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarVeiculoPorId(@PathVariable Long id) {
        Optional<Veiculo> veiculoOptional = veiculoService.buscarVeiculoPorId(id);
        return veiculoOptional
                .map(ResponseEntity::ok) // Se presente, retorna 200 OK com o veículo
                .orElseGet(() -> ResponseEntity.notFound().build()); // Se não, retorna 404 Not Found
    }

    // Endpoint para EDITAR um veículo existente (RF010)
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> editarVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculoDetalhes) {
        try {
            Veiculo veiculoAtualizado = veiculoService.editarVeiculo(id, veiculoDetalhes);
            return ResponseEntity.ok(veiculoAtualizado);
        } catch (RuntimeException e) { // Idealmente, capturar uma exceção mais específica como
                                       // ResourceNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para INATIVAR um veículo (RF010)
    // Usando POST para uma ação específica de inativação.
    // Alternativamente, poderia ser um PATCH no próprio recurso /api/veiculos/{id}
    // ou o PUT acima poderia ser usado para alterar o status para INATIVO.
    @PostMapping("/{id}/inativar")
    public ResponseEntity<Veiculo> inativarVeiculo(@PathVariable Long id) {
        try {
            Veiculo veiculoInativado = veiculoService.inativarVeiculo(id);
            return ResponseEntity.ok(veiculoInativado);
        } catch (RuntimeException e) { // Capturar exceção específica
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/liberar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> liberarVeiculo(@PathVariable Long id) {
        try {
            Veiculo veiculo = veiculoService.liberarVeiculoDaManutencao(id);
            return ResponseEntity.ok(veiculo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // Exemplo de endpoint para ATUALIZAR STATUS de forma mais genérica (se
    // necessário)
    // Poderia ser um PATCH para /api/veiculos/{id}/status ou similar
    /*
     * @PatchMapping("/{id}/status")
     * public ResponseEntity<Veiculo> atualizarStatusVeiculo(@PathVariable Long
     * id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
     * try {
     * Veiculo veiculo = veiculoService.atualizarStatusVeiculo(id,
     * statusUpdateRequest.getNovoStatus());
     * return ResponseEntity.ok(veiculo);
     * } catch (RuntimeException e) {
     * return ResponseEntity.notFound().build();
     * }
     * }
     * // Classe auxiliar para o request body do status (exemplo)
     * static class StatusUpdateRequest {
     * private StatusVeiculo novoStatus;
     * public StatusVeiculo getNovoStatus() { return novoStatus; }
     * public void setNovoStatus(StatusVeiculo novoStatus) { this.novoStatus =
     * novoStatus; }
     * }
     */
}