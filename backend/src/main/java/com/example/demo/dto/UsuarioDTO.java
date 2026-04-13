package com.example.demo.dto;

import com.example.demo.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String nomeUsuario;
    private String matricula;
    private String email;
    private TipoUsuario tipo;

    private List<Long> cursosIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> nomesCursos;
}