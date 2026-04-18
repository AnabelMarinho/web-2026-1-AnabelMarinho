package com.example.demo.model;

import com.example.demo.enums.DiaSemana;
import com.example.demo.enums.SolicitarSalaStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
public class SolicitarSala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario discente;

    @ManyToOne
    private Sala sala;

    @Enumerated(EnumType.STRING)
    private SolicitarSalaStatus status;

    @Enumerated(EnumType.STRING)
    private DiaSemana diaSemana;

    @Column(nullable = false)
    private LocalTime horarioInicio;

    @Column(nullable = false)
    private LocalTime horarioFim;

}
