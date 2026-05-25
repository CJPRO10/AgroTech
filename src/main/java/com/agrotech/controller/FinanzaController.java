package com.agrotech.controller;

import com.agrotech.dto.request.FinanzaRequestDTO;
import com.agrotech.dto.response.FinanzaResponseDTO;
import com.agrotech.service.FinanzaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finanzas")
public class FinanzaController {

    private final FinanzaService finanzaService;

    public FinanzaController(FinanzaService finanzaService) {
        this.finanzaService = finanzaService;
    }

    // RF19 — Registrar
    @PostMapping
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<FinanzaResponseDTO> crear(
            @RequestBody FinanzaRequestDTO dto, Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(finanzaService.crear(auth.getName(), dto));
    }

    // RF22 — Listar/Buscar
    @GetMapping
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<FinanzaResponseDTO>> listar(Authentication auth) {
        return ResponseEntity.ok(finanzaService.listar(auth.getName()));
    }

    // RF21 — Editar
    @PutMapping("/{idFinanza}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<FinanzaResponseDTO> actualizar(
            @PathVariable Integer idFinanza,
            @RequestBody FinanzaRequestDTO dto,
            Authentication auth) {
        return ResponseEntity.ok(finanzaService.actualizar(auth.getName(), idFinanza, dto));
    }

    // RF20 — Eliminar
    @DeleteMapping("/{idFinanza}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer idFinanza, Authentication auth) {
        finanzaService.eliminar(auth.getName(), idFinanza);
        return ResponseEntity.noContent().build();
    }

    // RF23 — Filtrar por tipo
    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<FinanzaResponseDTO>> filtrarPorTipo(
            @PathVariable String tipo, Authentication auth) {
        return ResponseEntity.ok(finanzaService.filtrarPorTipo(auth.getName(), tipo));
    }

    // RF24 — Filtrar por categoría
    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<FinanzaResponseDTO>> filtrarPorCategoria(
            @PathVariable String categoria, Authentication auth) {
        return ResponseEntity.ok(finanzaService.filtrarPorCategoria(auth.getName(), categoria));
    }
}
