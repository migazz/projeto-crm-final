package com.seuCRM.crm.controller;

import com.seuCRM.crm.config.JwtUtil;
import com.seuCRM.crm.entity.Usuario;
import com.seuCRM.crm.services.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    // Classe interna para receber os dados de login
    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String senha;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Tenta autenticar com email e senha
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()));

            // Se chegou aqui, autenticação foi bem sucedida — gera o token
            String token = jwtUtil.gerarToken(request.getEmail());

            // Busca o usuário para retornar informações junto com o token
            Usuario usuario = usuarioService.buscarPorEmail(request.getEmail())
                    .orElseThrow();

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", usuario.getEmail());
            response.put("nome", usuario.getNome());
            response.put("role", usuario.getRole());

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            // Email ou senha incorretos
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Email ou senha incorretos");
            return ResponseEntity.status(401).body(erro);
        }
    }

    // POST /api/auth/registro — cria o primeiro usuário admin
    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.getEmail().split("@")[0]); // usa parte do email como nome
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha());
        usuario.setRole(Usuario.Role.ADMIN);

        Usuario criado = usuarioService.criar(usuario);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Usuário criado com sucesso!");
        response.put("email", criado.getEmail());
        response.put("role", criado.getRole());

        return ResponseEntity.ok(response);
    }
}