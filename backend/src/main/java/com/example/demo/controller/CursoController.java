package com.example.demo.controller;

import com.example.demo.dto.CursoDTO;
import com.example.demo.service.CursoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    // Listar todos os cursos
    @GetMapping
    public List<CursoDTO> listar() {
        return cursoService.listarDTO();
    }

    // Buscar curso por ID
    @GetMapping("/{id}")
    public CursoDTO buscarPorId(@PathVariable Long id) {
        return cursoService.buscarPorIdDTO(id);
    }

    // Criar curso
    @PostMapping
    public CursoDTO salvar(@RequestBody CursoDTO dto) {
        return cursoService.salvarDTO(dto);
    }

    // Atualizar curso
    @PutMapping("/{id}")
    public CursoDTO atualizar(@PathVariable Long id, @RequestBody CursoDTO dto) {
        return cursoService.atualizarDTO(id, dto);
    }

    // Deletar curso
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        cursoService.deletar(id);
    }
}