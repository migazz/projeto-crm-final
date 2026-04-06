package com.seuCRM.crm.controller;

import com.seuCRM.crm.dto.UsuarioDTO;
import com.seuCRM.crm.entity.Usuario;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos()
                .stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO dto) {
        Usuario criado = usuarioService.criar(dto.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioDTO.fromEntity(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {
        Usuario atualizado = usuarioService.atualizar(id, dto.toEntity());
        return ResponseEntity.ok(UsuarioDTO.fromEntity(atualizado));
    }

    @PatchMapping("/{id}/ativo")
    public ResponseEntity<UsuarioDTO> alternarAtivo(@PathVariable Long id) {
        Usuario usuario = usuarioService.alternarAtivo(id);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}