package com.example.demo.model;

import com.example.demo.enums.CursoStatus;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    private Usuario instrutor;

    @Column(nullable = false, length = 100)
    private String nivel;

    @ManyToMany(mappedBy = "cursos")
    private List<Usuario> usuarios;

    @Enumerated(EnumType.STRING)
    private CursoStatus status;

}