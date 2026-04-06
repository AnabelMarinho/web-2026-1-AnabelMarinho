package com.example.demo.controller;

import com.example.demo.dto.SolicitarCursoDTO;
import com.example.demo.service.SolicitarCursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitarCursoController {

    private final SolicitarCursoService service;

    public SolicitarCursoController(SolicitarCursoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> solicitar(@RequestBody SolicitarCursoDTO dto) {
        service.solicitarMinistrar(dto.getUsuarioId(), dto.getCursoId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/aceitar")
    public ResponseEntity<Void> aceitar(@PathVariable Long id,
                                        @RequestParam Long docenteId) {
        service.aceitarSolicitacao(docenteId, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/recusar")
    public ResponseEntity<Void> recusar(@PathVariable Long id,
                                        @RequestParam Long docenteId) {
        service.recusarSolicitacao(docenteId, id);
        return ResponseEntity.ok().build();
    }
}