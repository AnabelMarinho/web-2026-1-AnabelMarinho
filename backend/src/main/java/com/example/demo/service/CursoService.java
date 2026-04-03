package com.example.demo.service;

import com.example.demo.dto.CursoDTO;
import com.example.demo.model.Curso;
import com.example.demo.repository.CursoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    // Converter Curso para DTO
    public CursoDTO toDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNomeCurso(curso.getNomeCurso());
        dto.setDescricao(curso.getDescricao());
        dto.setCategoria(curso.getCategoria());
        dto.setInstrutor(curso.getInstrutor());
        dto.setNivel(curso.getNivel());
        return dto;
    }

    // Converter DTO para Curso
    public Curso fromDTO(CursoDTO dto) {
        Curso curso = new Curso();
        curso.setNomeCurso(dto.getNomeCurso());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoria(dto.getCategoria());
        curso.setInstrutor(dto.getInstrutor());
        curso.setNivel(dto.getNivel());
        return curso;
    }

    // Listar todos os cursos (retornando DTOs)
    public List<CursoDTO> listarDTO() {
        return cursoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar curso por ID (retornando DTO)
    public CursoDTO buscarPorIdDTO(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));
        return toDTO(curso);
    }

    // Criar curso
    public CursoDTO salvarDTO(CursoDTO dto) {
        if (dto.getNomeCurso() == null || dto.getNomeCurso().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nome do curso é obrigatório");
        }
        Curso curso = fromDTO(dto);
        Curso salvo = cursoRepository.save(curso);
        return toDTO(salvo);
    }

    // Atualizar curso
    public CursoDTO atualizarDTO(Long id, CursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));

        curso.setNomeCurso(dto.getNomeCurso());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoria(dto.getCategoria());
        curso.setInstrutor(dto.getInstrutor());
        curso.setNivel(dto.getNivel());

        Curso atualizado = cursoRepository.save(curso);
        return toDTO(atualizado);
    }

    // Deletar curso
    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));
        cursoRepository.delete(curso);
    }
}