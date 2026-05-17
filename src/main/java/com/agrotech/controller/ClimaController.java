package com.agrotech.controller;

import com.agrotech.dto.response.ClimaResponseDTO;
import com.agrotech.dto.response.PronosticoResponseDTO;
import com.agrotech.service.ClimaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    // Consular clima actual de una ubicacion llama a Open-Meteo y guarda en BD
    @GetMapping("/actual/{idUbicacion}")
    public ResponseEntity<ClimaResponseDTO> climaActual(@PathVariable Integer idUbicacion) {
        return ResponseEntity.ok(climaService.consultarClimaActual(idUbicacion));
    }

    // Consultar pronostico de los proximos N días por defecto 7
    // GET /api/clima/pronostico/{idUbicacion}?dias=7
    @GetMapping("/pronostico/{idUbicacion}")
    public ResponseEntity<PronosticoResponseDTO> pronostico(
            @PathVariable Integer idUbicacion,
            @RequestParam(defaultValue = "7") int dias) {
        return ResponseEntity.ok(climaService.consultarPronostico(idUbicacion, dias));
    }

    // Consultar historial climatico completo de una ubicacion
    @GetMapping("/historial/{idUbicacion}")
    public ResponseEntity<List<ClimaResponseDTO>> historial(@PathVariable Integer idUbicacion) {
        return ResponseEntity.ok(climaService.consultarHistorial(idUbicacion));
    }

    // Consular historial filtrado por rango de fechas
    // GET /api/clima/historial/{idUbicacion}/rango?desde=2024-01-01&hasta=2024-12-31
    @GetMapping("/historial/{idUbicacion}/rango")
    public ResponseEntity<List<ClimaResponseDTO>> historialPorRango(
            @PathVariable Integer idUbicacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(climaService.consultarHistorialPorRango(idUbicacion, desde, hasta));
    }
}