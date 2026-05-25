package com.agrotech.controller;

import com.agrotech.dto.request.ReporteRequestDTO;
import com.agrotech.dto.response.ReporteResponseDTO;
import com.agrotech.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // RF45 — Generar reporte
    @PostMapping
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<ReporteResponseDTO> crear(
            @Valid @RequestBody ReporteRequestDTO dto,
            Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reporteService.crear(auth.getName(), dto));
    }

    // RF45 — Consultar reportes
    @GetMapping
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<ReporteResponseDTO>> listar(Authentication auth) {
        return ResponseEntity.ok(reporteService.listar(auth.getName()));
    }

    // Buscar por nombre
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<ReporteResponseDTO>> buscarPorNombre(
            @RequestParam String nombre, Authentication auth) {
        return ResponseEntity.ok(reporteService.buscarPorNombre(auth.getName(), nombre));
    }

    // RF46 — Filtrar por formato
    @GetMapping("/formato/{formato}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<ReporteResponseDTO>> filtrarPorFormato(
            @PathVariable String formato, Authentication auth) {
        return ResponseEntity.ok(reporteService.filtrarPorFormato(auth.getName(), formato));
    }

    // Filtrar por periodicidad
    @GetMapping("/periodicidad/{periodicidad}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<ReporteResponseDTO>> filtrarPorPeriodicidad(
            @PathVariable String periodicidad, Authentication auth) {
        return ResponseEntity.ok(reporteService.filtrarPorPeriodicidad(auth.getName(), periodicidad));
    }

    // RF47 — Eliminar reporte
    @DeleteMapping("/{idReporte}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Integer idReporte, Authentication auth) {
        reporteService.eliminar(auth.getName(), idReporte);
        return ResponseEntity.noContent().build();
    }
}
