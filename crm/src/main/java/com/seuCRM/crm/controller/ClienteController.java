package com.seuCRM.crm.controller;

import com.seuCRM.crm.dto.ClienteDTO;
import com.seuCRM.crm.entity.Cliente;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarTodos(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Cliente.Status status) {

        List<Cliente> clientes;
        if (nome != null && !nome.isBlank()) {
            clientes = clienteService.buscarPorNome(nome);
        } else if (status != null) {
            clientes = clienteService.buscarPorStatus(status);
        } else {
            clientes = clienteService.listarTodos();
        }

        List<ClienteDTO> dtos = clientes.stream()
                .map(ClienteDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
        return ResponseEntity.ok(ClienteDTO.fromEntity(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@Valid @RequestBody ClienteDTO dto) {
        Cliente criado = clienteService.criar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClienteDTO.fromEntity(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {
        Cliente atualizado = clienteService.atualizar(id, dto.toEntity());
        return ResponseEntity.ok(ClienteDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}