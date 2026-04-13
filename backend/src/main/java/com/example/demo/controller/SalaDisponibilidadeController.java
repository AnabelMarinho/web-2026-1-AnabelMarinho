package com.example.demo.controller;

import com.example.demo.dto.SalaDisponibilidadeDTO;
import com.example.demo.service.SalaDisponibilidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salas-disponibilidade")
public class SalaDisponibilidadeController {

    private final SalaDisponibilidadeService service;

    public SalaDisponibilidadeController(SalaDisponibilidadeService service) {
        this.service = service;
    }

    // Criar disponibilidade
    @PostMapping
    public ResponseEntity<SalaDisponibilidadeDTO> criar(@RequestBody SalaDisponibilidadeDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<SalaDisponibilidadeDTO>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    // Buscar por sala
    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<SalaDisponibilidadeDTO>> buscarPorSala(@PathVariable Long salaId) {
        return ResponseEntity.ok(service.buscarPorSala(salaId));
    }

    // Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}