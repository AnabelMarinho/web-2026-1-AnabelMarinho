package com.example.demo.dto;
import com.example.demo.model.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RegisterDTO {
    private String nomeUsuario;
    private String email;
    private String senha;
    private String matricula;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> cursosIds;

    private TipoUsuario tipo;
}
