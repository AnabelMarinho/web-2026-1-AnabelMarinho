package com.example.demo.service;

import com.example.demo.dto.UsuarioDTO;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    // Converter Usuario para DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeUsuario(usuario.getNomeUsuario());
        dto.setMatricula(usuario.getMatricula());
        dto.setTipo(usuario.getTipo());
        return dto;
    }

    // Converter DTO para Usuario (entidade)
    public Usuario fromDTO(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());
        return usuario;
    }

    // Listar todos usuários (DTOs)
    public List<UsuarioDTO> listarDTO() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar usuário por ID (DTO)
    public UsuarioDTO buscarPorIdDTO(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return toDTO(usuario);
    }

    // Criar usuário (DTO)
    public UsuarioDTO salvarDTO(UsuarioDTO dto) {
        Usuario usuario = fromDTO(dto);

        // apenas DISCENTE pode ter curso
        if (usuario.getTipo() != TipoUsuario.DISCENTE && usuario.getCurso() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Apenas discentes podem possuir curso");
        }

        // se tiver curso, verificar se existe
        if (usuario.getCurso() != null &&
                !cursoRepository.existsById(usuario.getCurso().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Curso não existe");
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return toDTO(salvo);
    }

    // Atualizar usuário (DTO)
    public UsuarioDTO atualizarDTO(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());

        //  apenas DISCENTE pode ter curso
        if (dto.getTipo() != TipoUsuario.DISCENTE && usuario.getCurso() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Apenas discentes podem possuir curso");
        }

        Usuario atualizado = usuarioRepository.save(usuario);
        return toDTO(atualizado);
    }

    // Deletar usuário
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }
}