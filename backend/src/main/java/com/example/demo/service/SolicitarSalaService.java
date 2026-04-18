package com.example.demo.service;

import com.example.demo.enums.DiaSemana;
import com.example.demo.enums.SolicitarSalaStatus;
import com.example.demo.enums.TipoUsuario;
import com.example.demo.model.*;
import com.example.demo.repository.SalaRepository;
import com.example.demo.repository.SolicitarSalaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;

@Service
public class SolicitarSalaService {

    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final SolicitarSalaRepository solicitacaoRepository;

    public SolicitarSalaService(UsuarioRepository usuarioRepository,
                                SalaRepository salaRepository,
                                SolicitarSalaRepository solicitacaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public void solicitarSala(Long usuarioId,
                              Long salaId,
                              DiaSemana diaSemana,
                              LocalTime inicio,
                              LocalTime fim) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (usuario.getTipo() != TipoUsuario.DISCENTE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas discentes podem solicitar salas");
        }

        Sala sala = salaRepository.findById(salaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala não encontrada"));

        boolean permitido = solicitacaoRepository
                .findDisponibilidadeValida(salaId, diaSemana, inicio, fim);

        if (!permitido) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sala não está disponível nesse dia e horário");
        }

        boolean conflito = sala.getTurmas().stream()
                .anyMatch(t ->
                        t.getSala() != null &&
                                inicio.isBefore(t.getHorarioFim()) &&
                                fim.isAfter(t.getHorarioInicio())
                );

        if (conflito) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Sala já está ocupada nesse horário");
        }

        SolicitarSala solicitacao = new SolicitarSala();
        solicitacao.setDiscente(usuario);
        solicitacao.setSala(sala);
        solicitacao.setDiaSemana(diaSemana);
        solicitacao.setHorarioInicio(inicio);
        solicitacao.setHorarioFim(fim);
        solicitacao.setStatus(SolicitarSalaStatus.PENDENTE);

        solicitacaoRepository.save(solicitacao);
    }

    public void aceitarSolicitacao(Long tecnicoId, Long solicitacaoId) {

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (tecnico.getTipo() != TipoUsuario.TECNICO) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas técnicos podem aceitar solicitações");
        }

        SolicitarSala solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Solicitação não encontrada"));

        if (solicitacao.getStatus() != SolicitarSalaStatus.PENDENTE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solicitação já foi processada");
        }

        solicitacao.setStatus(SolicitarSalaStatus.ACEITA);
        solicitacaoRepository.save(solicitacao);
    }

    public void recusarSolicitacao(Long tecnicoId, Long solicitacaoId) {

        Usuario tecnico = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (tecnico.getTipo() != TipoUsuario.TECNICO) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Apenas técnicos podem recusar solicitações");
        }

        SolicitarSala solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Solicitação não encontrada"));

        if (solicitacao.getStatus() != SolicitarSalaStatus.PENDENTE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solicitação já foi processada");
        }

        solicitacao.setStatus(SolicitarSalaStatus.RECUSADA);
        solicitacaoRepository.save(solicitacao);
    }
}