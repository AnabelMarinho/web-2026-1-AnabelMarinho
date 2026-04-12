package com.example.demo.controller;

import com.example.demo.dto.CursoDTO;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.CursoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;


    public CursoController(CursoService cursoService,  UsuarioRepository usuarioRepository) {
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
    public CursoDTO salvar(@RequestParam Long usuarioId,
                           @RequestBody CursoDTO dto) {
        return cursoService.criarCurso(usuarioId, dto);
    }

    // Atualizar curso
    @PutMapping("/{id}")
    public CursoDTO atualizar(@PathVariable Long id,
                              @RequestParam Long usuarioId,
                              @RequestBody CursoDTO dto) {
        return cursoService.atualizarCurso(usuarioId, id, dto);
    }

    // Deletar curso
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        cursoService.deletar(id);
    }

    // Sugerir curso (Docente)
    @PostMapping("/sugerir")
    public CursoDTO sugerir(@RequestParam Long usuarioId,
                            @RequestBody CursoDTO dto) {
        return cursoService.sugerirCurso(usuarioId, dto);
    }
}