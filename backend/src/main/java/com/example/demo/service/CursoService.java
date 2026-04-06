package com.example.demo.service;

import com.example.demo.dto.CursoDTO;
import com.example.demo.model.Curso;
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

    // Criar curso (AGORA COM REGRA NO LUGAR CERTO)
    public CursoDTO salvarDTO(CursoDTO dto) {
        if (dto.getNomeCurso() == null || dto.getNomeCurso().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nome do curso é obrigatório");
        }

        Curso curso = fromDTO(dto);

        if (dto.getInstrutorId() != null) {
            Usuario instrutor = usuarioRepository.findById(dto.getInstrutorId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Instrutor não encontrado"));

            if (instrutor.getTipo() != TipoUsuario.DISCENTE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Apenas discentes podem ser instrutores"
                );
            }

            curso.setInstrutor(instrutor);
        }

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
        curso.setNivel(dto.getNivel());

        if (dto.getInstrutorId() != null) {
            Usuario instrutor = usuarioRepository.findById(dto.getInstrutorId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Instrutor não encontrado"));

            if (instrutor.getTipo() != TipoUsuario.DISCENTE) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Apenas discentes podem ser instrutores"
                );
            }

            curso.setInstrutor(instrutor);
        } else {
            curso.setInstrutor(null);
        }

        Curso atualizado = cursoRepository.save(curso);
        return toDTO(atualizado);
    }

    // Deletar
    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));
        cursoRepository.delete(curso);
    }
}