package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(nullable = false, unique = true, length = 20)
    private String matricula;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

}