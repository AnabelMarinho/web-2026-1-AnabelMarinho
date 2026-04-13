package com.example.demo.controller;

import com.example.demo.dto.SalaDTO;
import com.example.demo.dto.SalaDisponibilidadeDTO;
import com.example.demo.enums.DiaSemana;
import com.example.demo.service.SalaDisponibilidadeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
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
    @GetMapping("/disponiveis")
    public List<SalaDTO> buscarDisponiveis(

            @RequestParam
            @Parameter(schema = @Schema(implementation = String.class), example = "SEGUNDA")
            DiaSemana diaSemana,

            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            @Parameter(
                    description = "Horário início (HH:mm)",
                    example = "13:20",
                    schema = @Schema(type = "string", format = "time")
            )
            LocalTime inicio,

            @RequestParam
            @DateTimeFormat(pattern = "HH:mm")
            @Parameter(
                    description = "Horário fim (HH:mm)",
                    example = "15:00",
                    schema = @Schema(type = "string", format = "time")
            )
            LocalTime fim
    ) {
        return service.buscarSalasDisponiveis(diaSemana, inicio, fim);
    }
}