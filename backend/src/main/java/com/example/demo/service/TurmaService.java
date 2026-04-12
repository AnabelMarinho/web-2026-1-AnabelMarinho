package com.example.demo.service;

import com.example.demo.dto.TurmaDTO;
import com.example.demo.model.*;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.TurmaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    public TurmaService(TurmaRepository turmaRepository,
                        CursoRepository cursoRepository,
                        UsuarioRepository usuarioRepository) {
        this.turmaRepository = turmaRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Converter Turma para DTO
    public TurmaDTO toDTO(Turma turma) {
        TurmaDTO dto = new TurmaDTO();

        dto.setId(turma.getId());
        dto.setNomeTurma(turma.getNomeTurma());
        dto.setDataInicio(turma.getDataInicio());
        dto.setDataFim(turma.getDataFim());
        dto.setHorarioInicio(turma.getHorarioInicio());
        dto.setHorarioFim(turma.getHorarioFim());
        dto.setSala(turma.getSala());

        dto.setCursoId(turma.getCurso().getId());
        dto.setNomeCurso(turma.getCurso().getNomeCurso());

        if (turma.getInstrutor() != null) {
            dto.setInstrutorId(turma.getInstrutor().getId());
            dto.setNomeInstrutor(turma.getInstrutor().getNomeUsuario());
        }

        return dto;
    }

    // Listar turmas de um curso
    public List<TurmaDTO> listarPorCurso(Long cursoId) {
        return turmaRepository.findByCursoId(cursoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar turma por ID
    public TurmaDTO buscarPorId(Long id) {
        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada"));

        return toDTO(turma);
    }

    // Criar turma
    public TurmaDTO criarTurma(String email, Long cursoId, TurmaDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));

        if (curso.getStatus() != CursoStatus.ATIVO) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Só é possível criar turmas para cursos ATIVOS");
        }

        if (!usuario.equals(curso.getInstrutor())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Apenas o instrutor do curso pode criar turmas");
        }

        Turma turma = new Turma();
        turma.setNomeTurma(dto.getNomeTurma());
        turma.setDataInicio(dto.getDataInicio());
        turma.setDataFim(dto.getDataFim());
        turma.setHorarioInicio(dto.getHorarioInicio());
        turma.setHorarioFim(dto.getHorarioFim());
        turma.setSala(dto.getSala());

        turma.setCurso(curso);
        turma.setInstrutor(usuario);

        return toDTO(turmaRepository.save(turma));
    }

    // Atualizar turma
    public TurmaDTO atualizarTurma(String email, Long id, TurmaDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada"));

        if (!usuario.equals(turma.getInstrutor())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Apenas o instrutor pode atualizar a turma");
        }

        turma.setNomeTurma(dto.getNomeTurma());
        turma.setDataInicio(dto.getDataInicio());
        turma.setDataFim(dto.getDataFim());
        turma.setHorarioInicio(dto.getHorarioInicio());
        turma.setHorarioFim(dto.getHorarioFim());
        turma.setSala(dto.getSala());

        return toDTO(turmaRepository.save(turma));
    }

    // Deletar turma
    public void deletarTurma(String email, Long id) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Turma turma = turmaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Turma não encontrada"));

        if (!usuario.equals(turma.getInstrutor())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Apenas o instrutor pode deletar a turma");
        }

        turmaRepository.delete(turma);
    }
}