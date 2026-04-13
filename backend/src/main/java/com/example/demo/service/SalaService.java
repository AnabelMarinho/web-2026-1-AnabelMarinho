package com.example.demo.service;

import com.example.demo.dto.SalaDTO;
import com.example.demo.model.Sala;
import com.example.demo.model.Turma;
import com.example.demo.repository.SalaRepository;
import com.example.demo.repository.TurmaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaService {

    private final SalaRepository salaRepository;
    private final TurmaRepository turmaRepository;

    public SalaService(SalaRepository salaRepository,
                       TurmaRepository turmaRepository) {
        this.salaRepository = salaRepository;
        this.turmaRepository = turmaRepository;
    }

    // Converter para DTO
    public SalaDTO toDTO(Sala sala) {
        SalaDTO dto = new SalaDTO();
        dto.setId(sala.getId());
        dto.setNomeSala(sala.getNomeSala());
        dto.setCapacidade(sala.getCapacidade());
        return dto;
    }

    // Listar todas as salas
    public List<SalaDTO> listarTodas() {
        return salaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public SalaDTO buscarPorId(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala não encontrada"));
        return toDTO(sala);
    }

    // Criar sala
    public SalaDTO criar(SalaDTO dto) {
        Sala sala = new Sala();
        sala.setNomeSala(dto.getNomeSala());
        sala.setCapacidade(dto.getCapacidade());

        return toDTO(salaRepository.save(sala));
    }

    //  Atualizar sala
    public SalaDTO atualizar(Long id, SalaDTO dto) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala não encontrada"));

        sala.setNomeSala(dto.getNomeSala());
        sala.setCapacidade(dto.getCapacidade());

        return toDTO(salaRepository.save(sala));
    }

    // Deletar sala
    public void deletar(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala não encontrada"));

        salaRepository.delete(sala);
    }

    // verificar se sala está disponivel
    public boolean salaDisponivel(Long salaId,
                                  LocalTime inicio,
                                  LocalTime fim) {

        List<Turma> turmas = turmaRepository.findAll();

        for (Turma t : turmas) {
            if (t.getSala().getId().equals(salaId)) {

                boolean conflito =
                        inicio.isBefore(t.getHorarioFim()) &&
                                fim.isAfter(t.getHorarioInicio());

                if (conflito) {
                    return false;
                }
            }
        }

        return true;
    }

    // Listar apenas salas disponíveis
    public List<SalaDTO> listarDisponiveis(LocalTime inicio,
                                           LocalTime fim) {

        return salaRepository.findAll()
                .stream()
                .filter(sala -> salaDisponivel(sala.getId(), inicio, fim))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}