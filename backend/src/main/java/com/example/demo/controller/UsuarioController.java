package com.example.demo.controller;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Listar todos usuários
    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listarDTO();
    }

    // Buscar usuário por ID
    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorIdDTO(id);
    }

    // Criar usuário
    @PostMapping
    public UsuarioDTO salvar(@RequestBody UsuarioDTO dto) {
        return usuarioService.salvarDTO(dto);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public UsuarioDTO atualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return usuarioService.atualizarDTO(id, dto);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }
}