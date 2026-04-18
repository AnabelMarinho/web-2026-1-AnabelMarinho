package com.example.demo.controller;

import com.example.demo.dto.SolicitarSalaDTO;
import com.example.demo.service.SolicitarSalaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/solicitacoes-sala")
public class SolicitarSalaController {

    private final SolicitarSalaService service;

    public SolicitarSalaController(SolicitarSalaService service) {
        this.service = service;
    }

   // criar
    @PostMapping
    public ResponseEntity<Void> solicitar(@RequestBody SolicitarSalaDTO dto) {

        service.solicitarSala(
                dto.getDiscenteId(),
                dto.getSalaId(),
                dto.getDiaSemana(),
                dto.getHorarioInicio(),
                dto.getHorarioFim()
        );

        return ResponseEntity.ok().build();
    }

    // aceitar
    @PutMapping("/{id}/aceitar")
    public ResponseEntity<Void> aceitar(@PathVariable Long id,
                                        @RequestParam Long tecnicoId) {

        service.aceitarSolicitacao(tecnicoId, id);
        return ResponseEntity.ok().build();
    }

    // recusar
    @PutMapping("/{id}/recusar")
    public ResponseEntity<Void> recusar(@PathVariable Long id,
                                        @RequestParam Long tecnicoId) {

        service.recusarSolicitacao(tecnicoId, id);
        return ResponseEntity.ok().build();
    }
}