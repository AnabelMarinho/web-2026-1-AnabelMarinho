package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeSala;

    @Column(nullable = false)
    private Integer capacidade;

    @OneToMany(mappedBy = "sala")
    private List<Turma> turmas;
}
