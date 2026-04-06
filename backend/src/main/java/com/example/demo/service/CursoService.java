package com.example.demo.service;

import com.example.demo.dto.CursoDTO;
import com.example.demo.model.Curso;
import com.example.demo.model.CursoStatus;
import com.example.demo.model.TipoUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public CursoService(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Converter Curso para DTO
    public CursoDTO toDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNomeCurso(curso.getNomeCurso());
        dto.setDescricao(curso.getDescricao());
        dto.setCategoria(curso.getCategoria());
        dto.setNivel(curso.getNivel());
        dto.setStatus(curso.getStatus());

        if (curso.getInstrutor() != null) {
            dto.setInstrutorId(curso.getInstrutor().getId());
            dto.setNomeInstrutor(curso.getInstrutor().getNomeUsuario());
        }

        return dto;
    }

    // Converter DTO para Curso (AGORA LIMPO, sem banco)
    public Curso fromDTO(CursoDTO dto) {
        Curso curso = new Curso();
        curso.setNomeCurso(dto.getNomeCurso());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoria(dto.getCategoria());
        curso.setNivel(dto.getNivel());

        return curso;
    }

    // Listar todos os cursos
    public List<CursoDTO> listarDTO() {
        return cursoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public CursoDTO buscarPorIdDTO(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));
        return toDTO(curso);
    }

    // Criar curso
    public CursoDTO criarCurso(Long usuarioId, CursoDTO dto) {
        Usuario discente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (discente.getTipo() != TipoUsuario.DISCENTE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas discentes podem criar cursos");
        }

        Curso curso = fromDTO(dto);
        curso.setInstrutor(discente);
        curso.setStatus(CursoStatus.ATIVO);

        return toDTO(cursoRepository.save(curso));
    }

    // Atualizar curso
    public CursoDTO atualizarCurso(Long usuarioId, Long id, CursoDTO dto) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));

        if (curso.getStatus() == CursoStatus.CONCLUIDO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Curso concluído não pode ser alterado");
        }

        if (usuario.getTipo() != TipoUsuario.DISCENTE || !usuario.equals(curso.getInstrutor())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas o instrutor pode atualizar o curso");
        }

        curso.setNomeCurso(dto.getNomeCurso());
        curso.setDescricao(dto.getDescricao());
        curso.setCategoria(dto.getCategoria());
        curso.setNivel(dto.getNivel());

        return toDTO(cursoRepository.save(curso));
    }

    // Deletar
    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));
        cursoRepository.delete(curso);
    }

    // Sugerir curso
    public CursoDTO sugerirCurso(Long usuarioId, CursoDTO dto) {

        Usuario docente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (docente.getTipo() != TipoUsuario.DOCENTE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas docentes podem sugerir cursos");
        }

        Curso curso = fromDTO(dto);
        curso.setStatus(CursoStatus.SUGERIDO);
        curso.setInstrutor(null);

        return toDTO(cursoRepository.save(curso));
    }

    // Concluir
    public void concluirCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));

        curso.setStatus(CursoStatus.CONCLUIDO);
        cursoRepository.save(curso);
    }
}