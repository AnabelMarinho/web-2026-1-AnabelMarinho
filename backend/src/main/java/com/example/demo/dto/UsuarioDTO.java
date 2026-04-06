package com.example.demo.dto;

import com.example.demo.model.TipoUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {

    private Long id;
    private String nomeUsuario;
    private String matricula;
    private String email;
    private TipoUsuario tipo;
    private Long cursoId;
    private String nomeCurso;
}