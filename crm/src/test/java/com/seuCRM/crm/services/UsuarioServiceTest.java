package com.seuCRM.crm.services;

import com.seuCRM.crm.entity.Usuario;
import com.seuCRM.crm.exception.BusinessException;
import com.seuCRM.crm.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioValido;

    @BeforeEach
    void setUp() {
        usuarioValido = new Usuario();
        usuarioValido.setId(1L);
        usuarioValido.setNome("Admin");
        usuarioValido.setEmail("admin@crm.com");
        usuarioValido.setSenha("senha123");
        usuarioValido.setRole(Usuario.Role.ADMIN);
        usuarioValido.setAtivo(true);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senha_criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        Usuario resultado = usuarioService.criar(usuarioValido);

        assertNotNull(resultado);
        assertEquals("Admin", resultado.getNome());
        // Verifica que a senha foi criptografada
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com email duplicado")
    void deveLancarExcecaoComEmailDuplicado() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        BusinessException excecao = assertThrows(
                BusinessException.class,
                () -> usuarioService.criar(usuarioValido));

        assertTrue(excecao.getMessage().contains("admin@crm.com"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar usuário por email com sucesso")
    void deveBuscarUsuarioPorEmail() {
        when(usuarioRepository.findByEmail("admin@crm.com"))
                .thenReturn(Optional.of(usuarioValido));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("admin@crm.com");

        assertTrue(resultado.isPresent());
        assertEquals("Admin", resultado.get().getNome());
    }

    @Test
    @DisplayName("Deve alternar status ativo do usuário")
    void deveAlternarStatusAtivo() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioValido));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioValido);

        usuarioValido.setAtivo(true);
        usuarioService.alternarAtivo(1L);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}