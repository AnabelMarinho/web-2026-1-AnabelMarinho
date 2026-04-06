package com.example.demo.dto;
import com.example.demo.model.TipoUsuario;
import lombok.Data;

@Data
public class RegisterDTO {
    private String nomeUsuario;
    private String email;
    private String senha;
    private String matricula;
    private Long cursoId;
    private TipoUsuario tipo;
}
