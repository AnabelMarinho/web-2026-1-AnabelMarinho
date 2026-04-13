package com.example.demo.service;

import com.example.demo.dto.SalaDTO;
import com.example.demo.dto.TurmaDTO;
import com.example.demo.enums.CursoStatus;
import com.example.demo.enums.DiaSemana;
import com.example.demo.enums.TipoSala;
import com.example.demo.model.*;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.TurmaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.repository.SalaRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final SalaDisponibilidadeService salaDisponibilidadeService;

    public TurmaService(TurmaRepository turmaRepository,
                        CursoRepository cursoRepository,
                        UsuarioRepository usuarioRepository, SalaRepository salaRepository, SalaDisponibilidadeService salaDisponibilidadeService) {
        this.turmaRepository = turmaRepository;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
        this.salaDisponibilidadeService = salaDisponibilidadeService;
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
        dto.setTipo(turma.getTipo());

        dto.setCursoId(turma.getCurso().getId());
        dto.setNomeCurso(turma.getCurso().getNomeCurso());

        if (turma.getInstrutor() != null) {
            dto.setInstrutorId(turma.getInstrutor().getId());
            dto.setNomeInstrutor(turma.getInstrutor().getNomeUsuario());
        }

        if (turma.getSala() != null) {
            dto.setSalaId(turma.getSala().getId());
            dto.setNomeSala(turma.getSala().getNomeSala());
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

        if (dto.getTipo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo da turma é obrigatório"
            );
        }

        Turma turma = new Turma();

        turma.setNomeTurma(dto.getNomeTurma());
        turma.setDataInicio(dto.getDataInicio());
        turma.setDataFim(dto.getDataFim());
        turma.setHorarioInicio(dto.getHorarioInicio());
        turma.setHorarioFim(dto.getHorarioFim());
        turma.setTipo(dto.getTipo());

        if (dto.getTipo() == TipoSala.PRESENCIAL) {

            if (dto.getSalaId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Turma presencial precisa de sala"
                );
            }

            Sala sala = salaRepository.findById(dto.getSalaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Sala não encontrada"));

            DiaSemana diaSemana = DiaSemana.valueOf(
                    dto.getDataInicio().getDayOfWeek().name()
            );

            List<SalaDTO> salasDisponiveis =
                    salaDisponibilidadeService.buscarSalasDisponiveis(
                            diaSemana,
                            dto.getHorarioInicio(),
                            dto.getHorarioFim()
                    );

            boolean disponivel = salasDisponiveis.stream()
                    .anyMatch(s -> s.getId().equals(sala.getId()));

            if (!disponivel) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Sala não está disponível nesse horário"
                );
            }

            turma.setSala(sala);

        } else {
            turma.setSala(null);
        }

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

        if (dto.getTipo() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Tipo da turma é obrigatório"
            );
        }

        turma.setNomeTurma(dto.getNomeTurma());
        turma.setDataInicio(dto.getDataInicio());
        turma.setDataFim(dto.getDataFim());
        turma.setHorarioInicio(dto.getHorarioInicio());
        turma.setHorarioFim(dto.getHorarioFim());
        turma.setTipo(dto.getTipo());

        if (dto.getTipo() == TipoSala.PRESENCIAL) {

            if (dto.getSalaId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Turma presencial precisa de sala"
                );
            }

            Sala sala = salaRepository.findById(dto.getSalaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Sala não encontrada"));

            // 🔥 VALIDAÇÃO CORRETA DE DISPONIBILIDADE
            DiaSemana diaSemana = DiaSemana.valueOf(
                    dto.getDataInicio().getDayOfWeek().name()
            );

            List<SalaDTO> salasDisponiveis =
                    salaDisponibilidadeService.buscarSalasDisponiveis(
                            diaSemana,
                            dto.getHorarioInicio(),
                            dto.getHorarioFim()
                    );

            boolean disponivel = salasDisponiveis.stream()
                    .anyMatch(s -> s.getId().equals(sala.getId()));

            if (!disponivel) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Sala não está disponível nesse horário"
                );
            }

            turma.setSala(sala);

        } else {
            turma.setSala(null);
        }

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