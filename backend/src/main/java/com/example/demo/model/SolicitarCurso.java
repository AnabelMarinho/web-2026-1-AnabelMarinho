package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SolicitarCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario discente;

    @ManyToOne
    private Curso curso;

    @Enumerated(EnumType.STRING)
    private SolicitarStatus status;
}
