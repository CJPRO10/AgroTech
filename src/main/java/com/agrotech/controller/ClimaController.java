package com.agrotech.controller;

import com.agrotech.dto.response.ClimaResponseDTO;
import com.agrotech.dto.response.PronosticoResponseDTO;
import com.agrotech.service.ClimaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clima")
public class ClimaController {

    private final ClimaService climaService;

    public ClimaController(ClimaService climaService) {
        this.climaService = climaService;
    }

    @GetMapping("/actual/{idUbicacion}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<ClimaResponseDTO> climaActual(@PathVariable Integer idUbicacion, Authentication authentication) {
        return ResponseEntity.ok(climaService.consultarClimaActual(idUbicacion));
    }

    @GetMapping("/pronostico/{idUbicacion}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<PronosticoResponseDTO> pronostico(@PathVariable Integer idUbicacion, @RequestParam(defaultValue = "7") int dias, Authentication authentication) {
        return ResponseEntity.ok(climaService.consultarPronostico(idUbicacion, dias));
    }

    @GetMapping("/historial/{idUbicacion}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<ClimaResponseDTO>> historial(@PathVariable Integer idUbicacion, Authentication authentication) {
        return ResponseEntity.ok(climaService.consultarHistorial(idUbicacion));
    }

    @GetMapping("/historial/{idUbicacion}/rango")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<ClimaResponseDTO>> historialPorRango(@PathVariable Integer idUbicacion, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta, Authentication authentication) {
        return ResponseEntity.ok(climaService.consultarHistorialPorRango(idUbicacion, desde, hasta));
    }

    @PostMapping("/actualizar/{idUbicacion}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<ClimaResponseDTO> actualizarClima(@PathVariable Integer idUbicacion, Authentication authentication) {
        return ResponseEntity.ok(climaService.consultarClimaActual(idUbicacion));
    }
}