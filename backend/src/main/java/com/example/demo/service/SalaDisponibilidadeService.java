package com.example.demo.service;

import com.example.demo.dto.SalaDTO;
import com.example.demo.dto.SalaDisponibilidadeDTO;
import com.example.demo.enums.DiaSemana;
import com.example.demo.model.Sala;
import com.example.demo.model.SalaDisponibilidade;
import com.example.demo.model.Turma;
import com.example.demo.repository.SalaDisponibilidadeRepository;
import com.example.demo.repository.SalaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
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

    // Converter entidade para DTO
    public SalaDisponibilidadeDTO toDTO(SalaDisponibilidade entity) {
        SalaDisponibilidadeDTO dto = new SalaDisponibilidadeDTO();

        dto.setId(entity.getId());
        dto.setSalaId(entity.getSala().getId());
        dto.setDiaSemana(entity.getDiaSemana());
        dto.setHorarioInicio(entity.getHorarioInicio());
        dto.setHorarioFim(entity.getHorarioFim());

        return dto;
    }

    // Criar disponibilidade
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

    // Listar todas
    public List<SalaDisponibilidadeDTO> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Buscar por sala
    public List<SalaDisponibilidadeDTO> buscarPorSala(Long salaId) {
        return repository.findBySalaId(salaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Deletar
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    // Buscar salas disponíveis
    public List<SalaDTO> buscarSalasDisponiveis(
            DiaSemana diaSemana,
            LocalTime inicio,
            LocalTime fim
    ) {

        List<Sala> salas = salaRepository.findAll();

        return salas.stream()
                .filter(sala -> {

                    boolean permitida = repository
                            .findBySalaIdAndDiaSemana(sala.getId(), diaSemana)
                            .stream()
                            .anyMatch(d ->
                                    !inicio.isBefore(d.getHorarioInicio()) &&
                                            !fim.isAfter(d.getHorarioFim())
                            );

                    if (!permitida) return false;

                    for (Turma t : sala.getTurmas()) {
                        if (t.getSala() == null) continue;

                        boolean conflito =
                                inicio.isBefore(t.getHorarioFim()) &&
                                        fim.isAfter(t.getHorarioInicio());

                        if (conflito) return false;
                    }

                    return true;
                })
                .map(this::toSalaDTO)
                .toList();
    }

    private SalaDTO toSalaDTO(Sala sala) {
        SalaDTO dto = new SalaDTO();
        dto.setId(sala.getId());
        dto.setNomeSala(sala.getNomeSala());
        dto.setCapacidade(sala.getCapacidade());
        return dto;
    }
}