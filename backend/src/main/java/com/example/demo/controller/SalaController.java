package com.example.demo.controller;

import com.example.demo.dto.SalaDTO;
import com.example.demo.service.SalaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    // Criar sala
    @PostMapping
    public ResponseEntity<SalaDTO> criar(@RequestBody SalaDTO dto) {
        return ResponseEntity.ok(salaService.criar(dto));
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<SalaDTO>> listar() {
        return ResponseEntity.ok(salaService.listarTodas());
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<SalaDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.buscarPorId(id));
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<SalaDTO> atualizar(@PathVariable Long id,
                                             @RequestBody SalaDTO dto) {
        return ResponseEntity.ok(salaService.atualizar(id, dto));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        salaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}