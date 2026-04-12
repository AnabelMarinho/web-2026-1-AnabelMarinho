package com.example.demo.repository;

import com.example.demo.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {
    List<Turma> findByCursoId(Long cursoId);
    List<Turma> findByInstrutorId(Long instrutorId);
}
