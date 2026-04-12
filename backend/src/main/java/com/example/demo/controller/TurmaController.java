package com.example.demo.controller;

import com.example.demo.dto.TurmaDTO;
import com.example.demo.service.TurmaService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    // Listar turmas de um curso
    @GetMapping("/cursos/{cursoId}/turmas")
    public List<TurmaDTO> listarPorCurso(@PathVariable Long cursoId) {
        return turmaService.listarPorCurso(cursoId);
    }

    // Buscar turma por ID
    @GetMapping("/turmas/{id}")
    public TurmaDTO buscarPorId(@PathVariable Long id) {
        return turmaService.buscarPorId(id);
    }

    // Criar turma
    @PostMapping("/cursos/{cursoId}/turmas")
    public TurmaDTO criarTurma(
            @PathVariable Long cursoId,
            @RequestBody TurmaDTO dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        return turmaService.criarTurma(email, cursoId, dto);
    }

    // Atualizar turma
    @PutMapping("/turmas/{id}")
    public TurmaDTO atualizarTurma(
            @PathVariable Long id,
            @RequestBody TurmaDTO dto,
            Authentication authentication
    ) {
        return turmaService.atualizarTurma(authentication.getName(), id, dto);
    }

    // Deletar turma
    @DeleteMapping("/turmas/{id}")
    public void deletarTurma(
            @PathVariable Long id,
            Authentication authentication
    ) {
        turmaService.deletarTurma(authentication.getName(), id);
    }
}