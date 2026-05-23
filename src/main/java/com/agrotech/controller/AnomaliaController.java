package com.agrotech.controller;

import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;
import com.agrotech.dto.request.AnomaliaRequestDTO;
import com.agrotech.dto.response.AnomaliaResponseDTO;
import com.agrotech.service.AnomaliaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/anomalias")
public class AnomaliaController {

    private final AnomaliaService anomaliaService;

    public AnomaliaController(AnomaliaService anomaliaService) {
        this.anomaliaService = anomaliaService;
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<AnomaliaResponseDTO> registrar(@RequestBody AnomaliaRequestDTO dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anomaliaService.registrar(dto, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<AnomaliaResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.listar(authentication.getName()));
    }

    @GetMapping("/{idAnomalia}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<AnomaliaResponseDTO> buscarPorId(@PathVariable Integer idAnomalia, Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.buscarPorId(idAnomalia, authentication.getName()));
    }

    @GetMapping("/siembra/{idSiembra}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorSiembra(@PathVariable Integer idSiembra, Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.listarPorSiembra(idSiembra, authentication.getName()));
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorTipo(@PathVariable TipoAnomalia tipo, Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.listarPorTipo(tipo, authentication.getName()));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorEstado(@PathVariable EstadoAnomalia estado, Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.listarPorEstado(estado, authentication.getName()));
    }

    @GetMapping("/severidad/{nivelSeveridad}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorSeveridad(@PathVariable NivelSeveridad nivelSeveridad, Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.listarPorNivelSeveridad(nivelSeveridad, authentication.getName()));
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorFechas(@RequestParam LocalDateTime desde, @RequestParam(required = false) LocalDateTime hasta, Authentication authentication) {
        LocalDateTime fechaHasta = hasta != null ? hasta : LocalDateTime.now();
        return ResponseEntity.ok(anomaliaService.listarPorRangoFechas(desde, fechaHasta, authentication.getName()));
    }

    @PutMapping("/{idAnomalia}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<AnomaliaResponseDTO> actualizar(@PathVariable Integer idAnomalia, @RequestBody AnomaliaRequestDTO dto, Authentication authentication) {
        return ResponseEntity.ok(anomaliaService.actualizar(idAnomalia, dto, authentication.getName()));
    }

    @DeleteMapping("/{idAnomalia}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAnomalia, Authentication authentication) {
        anomaliaService.eliminar(idAnomalia, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}