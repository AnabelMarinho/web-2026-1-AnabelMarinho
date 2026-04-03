package com.example.demo.controller;

import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // REGISTRAR
    @PostMapping("/register")
    public Usuario register(@RequestBody RegisterDTO dto) {
        return usuarioService.registrar(dto);
    }

    // LOGIN (simples por enquanto)
    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        return usuarioService.login(dto);
    }
}