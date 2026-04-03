package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoDTO {
    private Long id;
    private String nomeCurso;
    private String descricao;
    private String categoria;
    private String instrutor;
    private String nivel;
}
