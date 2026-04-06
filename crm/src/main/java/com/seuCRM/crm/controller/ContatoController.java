package com.seuCRM.crm.controller;

import com.seuCRM.crm.dto.ContatoDTO;
import com.seuCRM.crm.entity.Contato;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.services.ContatoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes/{clienteId}/contatos")
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping
    public ResponseEntity<List<ContatoDTO>> listarPorCliente(@PathVariable Long clienteId) {
        List<ContatoDTO> contatos = contatoService.listarPorCliente(clienteId)
                .stream()
                .map(ContatoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoDTO> buscarPorId(@PathVariable Long id) {
        Contato contato = contatoService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato", id));
        return ResponseEntity.ok(ContatoDTO.fromEntity(contato));
    }

    @PostMapping
    public ResponseEntity<ContatoDTO> criar(
            @PathVariable Long clienteId,
            @Valid @RequestBody ContatoDTO dto) {
        Contato criado = contatoService.criar(clienteId, dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ContatoDTO.fromEntity(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContatoDTO dto) {
        Contato atualizado = contatoService.atualizar(id, dto.toEntity());
        return ResponseEntity.ok(ContatoDTO.fromEntity(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        contatoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}