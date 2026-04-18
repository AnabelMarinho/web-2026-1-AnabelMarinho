package com.example.demo.repository;

import com.example.demo.enums.DiaSemana;
import com.example.demo.enums.SolicitarSalaStatus;
import com.example.demo.model.SolicitarSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface SolicitarSalaRepository extends JpaRepository<SolicitarSala, Long> {

    List<SolicitarSala> findByStatus(SolicitarSalaStatus status);

    List<SolicitarSala> findByDiscenteId(Long usuarioId);

    // ======================================
    // VALIDA DISPONIBILIDADE DA SALA
    // ======================================
    @Query("""
        SELECT CASE WHEN COUNT(s) = 0 THEN true ELSE false END
        FROM SalaDisponibilidade s
        WHERE s.sala.id = :salaId
        AND s.diaSemana = :diaSemana
        AND :inicio >= s.horarioInicio
        AND :fim <= s.horarioFim
    """)
    boolean findDisponibilidadeValida(
            @Param("salaId") Long salaId,
            @Param("diaSemana") DiaSemana diaSemana,
            @Param("inicio") LocalTime inicio,
            @Param("fim") LocalTime fim
    );
}