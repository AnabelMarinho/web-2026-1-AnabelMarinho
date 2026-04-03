package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos
    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    // Criar usuário
    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNomeUsuario(usuarioAtualizado.getNomeUsuario());
        usuario.setCurso(usuarioAtualizado.getCurso());
        usuario.setMatricula(usuarioAtualizado.getMatricula());
        usuario.setTipo(usuarioAtualizado.getTipo());

        return usuarioRepository.save(usuario);
    }

    // Deletar usuário
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}