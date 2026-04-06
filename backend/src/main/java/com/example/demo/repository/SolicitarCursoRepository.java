package com.example.demo.repository;

import com.example.demo.model.SolicitarCurso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitarCursoRepository  extends JpaRepository<SolicitarCurso, Long> {
    boolean existsByDiscenteIdAndCursoId(Long discenteId, Long cursoId);
}
