package com.seuCRM.crm.services;

import com.seuCRM.crm.entity.Usuario;
import com.seuCRM.crm.exception.BusinessException;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario criar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + usuario.getEmail());
        }
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        if (usuario.getRole() == null) {
            usuario.setRole(Usuario.Role.USER);
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        usuario.setNome(dadosNovos.getNome());

        if (!usuario.getEmail().equals(dadosNovos.getEmail())) {
            if (usuarioRepository.existsByEmail(dadosNovos.getEmail())) {
                throw new BusinessException("Email já em uso: " + dadosNovos.getEmail());
            }
            usuario.setEmail(dadosNovos.getEmail());
        }
        if (dadosNovos.getSenha() != null && !dadosNovos.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario alternarAtivo(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuario.setAtivo(!usuario.getAtivo());
        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", id);
        }
        usuarioRepository.deleteById(id);
    }
}