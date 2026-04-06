package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.model.TipoUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          CursoRepository cursoRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeUsuario(usuario.getNomeUsuario());
        dto.setMatricula(usuario.getMatricula());
        dto.setEmail(usuario.getEmail());
        dto.setTipo(usuario.getTipo());

        if (usuario.getCurso() != null) {
            dto.setCursoId(usuario.getCurso().getId());
            dto.setNomeCurso(usuario.getCurso().getNomeCurso());
        }

        return dto;
    }

    public Usuario fromDTO(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());
        usuario.setEmail(dto.getEmail());
        return usuario;
    }

    public List<UsuarioDTO> listarDTO() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorIdDTO(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return toDTO(usuario);
    }

    public UsuarioDTO atualizarDTO(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setEmail(dto.getEmail());
        usuario.setTipo(dto.getTipo());

        if (dto.getCursoId() != null && dto.getCursoId() > 0) {
            usuario.setCurso(
                    cursoRepository.findById(dto.getCursoId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "Curso não encontrado"))
            );
        } else {
            usuario.setCurso(null);
        }

        if (usuario.getTipo() == TipoUsuario.DISCENTE && usuario.getCurso() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Discente precisa de curso");
        }

        if (usuario.getTipo() != TipoUsuario.DISCENTE && usuario.getCurso() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas discentes podem possuir curso");
        }

        Usuario atualizado = usuarioRepository.save(usuario);
        return toDTO(atualizado);
    }

    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }

    public Usuario registrar(RegisterDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setMatricula(dto.getMatricula());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setTipo(dto.getTipo());

        if (dto.getCursoId() != null && dto.getCursoId() > 0) {
            usuario.setCurso(
                    cursoRepository.findById(dto.getCursoId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "Curso não encontrado"))
            );
        } else {
            usuario.setCurso(null);
        }

        return usuarioRepository.save(usuario);
    }

    public String login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        }

        return jwtService.gerarToken(usuario.getEmail());
    }
}