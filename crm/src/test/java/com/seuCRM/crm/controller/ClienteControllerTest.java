package com.seuCRM.crm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seuCRM.crm.config.JwtUtil;
import com.seuCRM.crm.dto.ClienteDTO;
import com.seuCRM.crm.entity.Cliente;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.services.ClienteService;
import com.seuCRM.crm.services.UsuarioDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    private Cliente clienteValido;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setId(1L);
        clienteValido.setNome("Empresa Teste");
        clienteValido.setCnpj("12.345.678/0001-99");
        clienteValido.setEmail("teste@empresa.com");
        clienteValido.setStatus(Cliente.Status.ATIVO);

        clienteDTO = new ClienteDTO();
        clienteDTO.setNome("Empresa Teste");
        clienteDTO.setCnpj("12.345.678/0001-99");
        clienteDTO.setEmail("teste@empresa.com");
        clienteDTO.setStatus(Cliente.Status.ATIVO);
    }

    @Test
    @DisplayName("GET /api/clientes deve retornar lista de clientes")
    @WithMockUser
    void deveListarClientes() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(clienteValido));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Empresa Teste"))
                .andExpect(jsonPath("$[0].status").value("ATIVO"));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} deve retornar cliente por ID")
    @WithMockUser
    void deveBuscarClientePorId() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(clienteValido));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Empresa Teste"));
    }

    @Test
    @DisplayName("GET /api/clientes/{id} deve retornar 404 para ID inexistente")
    @WithMockUser
    void deveRetornar404ParaClienteInexistente() throws Exception {
        when(clienteService.buscarPorId(999L))
                .thenThrow(new ResourceNotFoundException("Cliente", 999L));

        mockMvc.perform(get("/api/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"));
    }

    @Test
    @DisplayName("POST /api/clientes deve criar cliente e retornar 201")
    @WithMockUser
    void deveCriarCliente() throws Exception {
        when(clienteService.criar(any(Cliente.class))).thenReturn(clienteValido);

        // csrf() adiciona o token CSRF necessário para POST nos testes
        mockMvc.perform(post("/api/clientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Empresa Teste"));
    }

    @Test
    @DisplayName("POST /api/clientes deve retornar 400 sem nome")
    @WithMockUser
    void deveRetornar400SemNome() throws Exception {
        clienteDTO.setNome("");

        mockMvc.perform(post("/api/clientes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} deve retornar 204")
    @WithMockUser
    void deveDeletarCliente() throws Exception {
        mockMvc.perform(delete("/api/clientes/1")
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/clientes sem autenticação deve retornar 401")
    void deveRetornar401SemAutenticacao() throws Exception {
        // Sem autenticação o Spring retorna 401 (não autorizado)
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isUnauthorized());
    }
}