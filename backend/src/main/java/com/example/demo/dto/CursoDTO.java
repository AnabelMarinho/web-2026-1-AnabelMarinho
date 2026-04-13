package com.example.demo.dto;

import com.example.demo.enums.CursoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoDTO {

    private String nomeCurso;
    private String descricao;
    private String categoria;
    private String nivel;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long instrutorId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String nomeInstrutor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CursoStatus status;
}
