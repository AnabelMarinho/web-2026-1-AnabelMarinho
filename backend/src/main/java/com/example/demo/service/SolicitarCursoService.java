package com.example.demo.service;

import com.example.demo.enums.CursoStatus;
import com.example.demo.enums.SolicitarCursoStatus;
import com.example.demo.enums.TipoUsuario;
import com.example.demo.model.*;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.SolicitarCursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class SolicitarCursoService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final SolicitarCursoRepository solicitacaoRepository;

    public SolicitarCursoService(UsuarioRepository usuarioRepository,
                                 CursoRepository cursoRepository,
                                 SolicitarCursoRepository solicitacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public void solicitarMinistrar(Long usuarioId, Long cursoId) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (usuario.getTipo() != TipoUsuario.DISCENTE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Apenas discentes podem solicitar");
        }

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Curso não encontrado"));

        if (curso.getStatus() != CursoStatus.SUGERIDO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Curso não está disponível para solicitação");
        }

        if (solicitacaoRepository.existsByDiscenteIdAndCursoId(usuarioId, cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Você já solicitou este curso");
        }

        SolicitarCurso solicitacao = new SolicitarCurso();
        solicitacao.setDiscente(usuario);
        solicitacao.setCurso(curso);
        solicitacao.setStatus(SolicitarCursoStatus.PENDENTE);

        solicitacaoRepository.save(solicitacao);
    }

    public void aceitarSolicitacao(Long docenteId, Long solicitacaoId) {

        Usuario docente = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Docente não encontrado"));

        if (docente.getTipo() != TipoUsuario.DOCENTE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Apenas docentes podem aceitar solicitações");
        }

        SolicitarCurso solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Solicitação não encontrada"));

        if (solicitacao.getStatus() != SolicitarCursoStatus.PENDENTE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Solicitação já foi processada");
        }

        Curso curso = solicitacao.getCurso();

        if (curso.getStatus() != CursoStatus.SUGERIDO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Curso não está mais disponível");
        }

        // define instrutor e ativa curso
        curso.setInstrutor(solicitacao.getDiscente());
        curso.setStatus(CursoStatus.ATIVO);

        // atualiza solicitação
        solicitacao.setStatus(SolicitarCursoStatus.ACEITA);

        cursoRepository.save(curso);
        solicitacaoRepository.save(solicitacao);
    }

    public void recusarSolicitacao(Long docenteId, Long solicitacaoId) {

        Usuario docente = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Docente não encontrado"));

        if (docente.getTipo() != TipoUsuario.DOCENTE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Apenas docentes podem recusar solicitações");
        }

        SolicitarCurso solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Solicitação não encontrada"));

        if (solicitacao.getStatus() != SolicitarCursoStatus.PENDENTE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Solicitação já foi processada");
        }

        solicitacao.setStatus(SolicitarCursoStatus.RECUSADA);

        solicitacaoRepository.save(solicitacao);
    }
}