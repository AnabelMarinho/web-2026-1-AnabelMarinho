package com.example.demo.repository;

import com.example.demo.model.Sala;
import com.example.demo.enums.TipoSala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {

    List<Sala> findByNomeSalaContainingIgnoreCase(String nome);
    List<Sala> findByCapacidadeGreaterThanEqual(Integer capacidade);
}