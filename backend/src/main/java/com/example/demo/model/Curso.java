package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeCurso;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(nullable = false, length = 100)
    private String instrutor;

    @Column(nullable = false, length = 100)
    private String nivel;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Usuario> usuarios;

}
