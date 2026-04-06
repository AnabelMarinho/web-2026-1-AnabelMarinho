package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UsuarioDTO;
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

    // Converter para DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeUsuario(usuario.getNomeUsuario());
        dto.setMatricula(usuario.getMatricula());
        dto.setEmail(usuario.getEmail());
        dto.setTipo(usuario.getTipo());

        if (usuario.getCursos() != null && !usuario.getCursos().isEmpty()) {
            dto.setCursosIds(
                    usuario.getCursos().stream()
                            .map(c -> c.getId())
                            .toList()
            );

            dto.setNomesCursos(
                    usuario.getCursos().stream()
                            .map(c -> c.getNomeCurso())
                            .toList()
            );
        }

        return dto;
    }

    //  Converter de DTO
    public Usuario fromDTO(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());
        usuario.setEmail(dto.getEmail());
        return usuario;
    }

    //  Listar
    public List<UsuarioDTO> listarDTO() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Buscar por ID
    public UsuarioDTO buscarPorIdDTO(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return toDTO(usuario);
    }

    // Atualizar
    public UsuarioDTO atualizarDTO(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setEmail(dto.getEmail());
        usuario.setTipo(dto.getTipo());

        //  múltiplos cursos
        if (dto.getCursosIds() != null) {
            usuario.setCursos(
                    dto.getCursosIds().stream()
                            .map(cursoId -> cursoRepository.findById(cursoId)
                                    .orElseThrow(() -> new ResponseStatusException(
                                            HttpStatus.BAD_REQUEST, "Curso não encontrado")))
                            .collect(Collectors.toList())
            );
        }

        Usuario atualizado = usuarioRepository.save(usuario);
        return toDTO(atualizado);
    }

    // Deletar
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }

    // Registrar
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

        // múltiplos cursos
        if (dto.getCursosIds() != null) {
            usuario.setCursos(
                    dto.getCursosIds().stream()
                            .map(cursoId -> cursoRepository.findById(cursoId)
                                    .orElseThrow(() -> new ResponseStatusException(
                                            HttpStatus.BAD_REQUEST, "Curso não encontrado")))
                            .collect(Collectors.toList())
            );
        }

        return usuarioRepository.save(usuario);
    }

    // Login
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