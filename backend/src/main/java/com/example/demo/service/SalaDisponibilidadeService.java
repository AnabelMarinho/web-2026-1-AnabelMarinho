package com.example.demo.service;

import com.example.demo.dto.SalaDisponibilidadeDTO;
import com.example.demo.model.Sala;
import com.example.demo.model.SalaDisponibilidade;
import com.example.demo.repository.SalaDisponibilidadeRepository;
import com.example.demo.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SalaDisponibilidadeService {
    private final SalaDisponibilidadeRepository repository;
    private final SalaRepository salaRepository;

    public SalaDisponibilidadeService(SalaDisponibilidadeRepository repository,
                                      SalaRepository salaRepository) {
        this.repository = repository;
        this.salaRepository = salaRepository;
    }

    public SalaDisponibilidadeDTO toDTO(SalaDisponibilidade entity) {
        SalaDisponibilidadeDTO dto = new SalaDisponibilidadeDTO();

        dto.setId(entity.getId());
        dto.setSalaId(entity.getSala().getId());
        dto.setDiaSemana(entity.getDiaSemana());
        dto.setHorarioInicio(entity.getHorarioInicio());
        dto.setHorarioFim(entity.getHorarioFim());

        return dto;
    }

    public SalaDisponibilidadeDTO criar(SalaDisponibilidadeDTO dto) {

        Sala sala = salaRepository.findById(dto.getSalaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala não encontrada"));

        SalaDisponibilidade entity = new SalaDisponibilidade();
        entity.setSala(sala);
        entity.setDiaSemana(dto.getDiaSemana());
        entity.setHorarioInicio(dto.getHorarioInicio());
        entity.setHorarioFim(dto.getHorarioFim());

        return toDTO(repository.save(entity));
    }

    public List<SalaDisponibilidadeDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public List<SalaDisponibilidadeDTO> buscarPorSala(Long salaId) {
        return repository.findBySalaId(salaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
