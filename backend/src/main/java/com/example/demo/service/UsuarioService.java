package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.model.TipoUsuario;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CursoRepository;
import com.example.demo.repository.UsuarioRepository;
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

    public UsuarioService(UsuarioRepository usuarioRepository,
                          CursoRepository cursoRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Converter Usuario → DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNomeUsuario(usuario.getNomeUsuario());
        dto.setMatricula(usuario.getMatricula());
        dto.setTipo(usuario.getTipo());
        return dto;
    }

    // Converter DTO → Usuario
    public Usuario fromDTO(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());
        return usuario;
    }

    // Listar usuários
    public List<UsuarioDTO> listarDTO() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    //  Buscar por ID
    public UsuarioDTO buscarPorIdDTO(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return toDTO(usuario);
    }

    // Criar usuário (via proDTO comum)
    public UsuarioDTO salvarDTO(UsuarioDTO dto) {
        Usuario usuario = fromDTO(dto);

        if (usuario.getTipo() != TipoUsuario.DISCENTE && usuario.getCurso() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Apenas discentes podem possuir curso");
        }

        if (usuario.getCurso() != null &&
                !cursoRepository.existsById(usuario.getCurso().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Curso não existe");
        }

        Usuario salvo = usuarioRepository.save(usuario);
        return toDTO(salvo);
    }

    // Atualizar usuário
    public UsuarioDTO atualizarDTO(Long id, UsuarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNomeUsuario(dto.getNomeUsuario());
        usuario.setMatricula(dto.getMatricula());
        usuario.setTipo(dto.getTipo());

        if (dto.getTipo() != TipoUsuario.DISCENTE && usuario.getCurso() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Apenas discentes podem possuir curso");
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

    // REGISTRO (cadastro com senha)
    public Usuario registrar(RegisterDTO dto) {

        if (usuarioRepository.findByEmail(dto.getEmail()) != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        Usuario usuario = new Usuario();

        usuario.setNomeUsuario(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setMatricula(dto.getMatricula());

        // criptografar senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        usuario.setTipo(TipoUsuario.DISCENTE);

        return usuarioRepository.save(usuario);
    }

    // LOGIN
    public String login(LoginDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail());

        if (usuario == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Senha inválida");
        }

        return "Login realizado com sucesso";
    }
}