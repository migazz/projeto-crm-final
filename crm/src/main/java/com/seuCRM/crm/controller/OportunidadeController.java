package com.seuCRM.crm.controller;

import com.seuCRM.crm.dto.OportunidadeDTO;
import com.seuCRM.crm.entity.Oportunidade;
import com.seuCRM.crm.exception.ResourceNotFoundException;
import com.seuCRM.crm.services.OportunidadeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/oportunidades")
public class OportunidadeController {

    private final OportunidadeService oportunidadeService;

    public OportunidadeController(OportunidadeService oportunidadeService) {
        this.oportunidadeService = oportunidadeService;
    }

    @GetMapping
    public ResponseEntity<List<OportunidadeDTO>> listarTodas(
            @RequestParam(required = false) Oportunidade.Estagio estagio,
            @RequestParam(required = false) Long clienteId) {

        List<Oportunidade> oportunidades;
        if (clienteId != null) {
            oportunidades = oportunidadeService.listarPorCliente(clienteId);
        } else if (estagio != null) {
            oportunidades = oportunidadeService.listarPorEstagio(estagio);
        } else {
            oportunidades = oportunidadeService.listarTodas();
        }

        return ResponseEntity.ok(oportunidades.stream()
                .map(OportunidadeDTO::fromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OportunidadeDTO> buscarPorId(@PathVariable Long id) {
        Oportunidade op = oportunidadeService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oportunidade", id));
        return ResponseEntity.ok(OportunidadeDTO.fromEntity(op));
    }

    @PostMapping
    public ResponseEntity<OportunidadeDTO> criar(@Valid @RequestBody OportunidadeDTO dto) {
        Oportunidade criada = oportunidadeService.criar(
                dto.getClienteId(),
                dto.toEntity(),
                dto.getResponsavelId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OportunidadeDTO.fromEntity(criada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OportunidadeDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OportunidadeDTO dto) {
        Oportunidade atualizada = oportunidadeService.atualizar(
                id,
                dto.toEntity(),
                dto.getClienteId());
        return ResponseEntity.ok(OportunidadeDTO.fromEntity(atualizada));
    }

    @PatchMapping("/{id}/avancar")
    public ResponseEntity<OportunidadeDTO> avancarEstagio(@PathVariable Long id) {
        Oportunidade op = oportunidadeService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Oportunidade", id));

        Oportunidade.Estagio[] estagios = Oportunidade.Estagio.values();
        int atual = op.getEstagio().ordinal();
        if (atual < estagios.length - 1) {
            op.setEstagio(estagios[atual + 1]);
        }

        Oportunidade atualizada = oportunidadeService.atualizar(
                id,
                op,
                op.getCliente().getId());
        return ResponseEntity.ok(OportunidadeDTO.fromEntity(atualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        oportunidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}