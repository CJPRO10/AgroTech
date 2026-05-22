package com.agrotech.controller;

import com.agrotech.dto.request.SiembraRequestDTO;
import com.agrotech.dto.request.SiembraUpdateRequestDTO;
import com.agrotech.dto.response.SiembraResponseDTO;
import com.agrotech.service.SiembraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/siembras")
public class SiembraController {

    private final SiembraService siembraService;

    public SiembraController(SiembraService siembraService) {
        this.siembraService = siembraService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<SiembraResponseDTO> crear(@RequestBody SiembraRequestDTO dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(siembraService.crear(dto, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(siembraService.listar(authentication.getName()));
    }

    @GetMapping("/finca/{idFinca}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> buscarPorFinca(@PathVariable Integer idFinca, Authentication authentication) {
        return ResponseEntity.ok(siembraService.buscarPorFinca(idFinca, authentication.getName()));
    }

    @GetMapping("/cultivo/{idCultivo}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> buscarPorCultivo(@PathVariable Integer idCultivo, Authentication authentication) {
        return ResponseEntity.ok(siembraService.buscarPorCultivo(idCultivo, authentication.getName()));
    }

    @GetMapping("/estado/{idEstado}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> buscarPorEstado(@PathVariable Integer idEstado, Authentication authentication) {
        return ResponseEntity.ok(siembraService.buscarPorEstado(idEstado, authentication.getName()));
    }

    @GetMapping("/finca/{idFinca}/cultivo/{idCultivo}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> buscarPorFincaYCultivo(@PathVariable Integer idFinca, @PathVariable Integer idCultivo, Authentication authentication) {
        return ResponseEntity.ok(siembraService.buscarPorFincaYCultivo(idFinca, idCultivo, authentication.getName()));
    }

    @GetMapping("/finca/{idFinca}/lote/{numLote}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> buscarPorFincaYLote(@PathVariable Integer idFinca, @PathVariable Integer numLote, Authentication authentication) {
        return ResponseEntity.ok(siembraService.buscarPorFincaYLote(idFinca, numLote, authentication.getName()));
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraResponseDTO>> buscarPorFechas(@RequestParam LocalDateTime desde, @RequestParam(required = false) LocalDateTime hasta, Authentication authentication) {
        LocalDateTime fechaHasta = hasta != null ? hasta : LocalDateTime.now();
        return ResponseEntity.ok(siembraService.buscarPorRangoFechas(desde, fechaHasta, authentication.getName()));
    }

    @PutMapping("/{idSiembra}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<SiembraResponseDTO> actualizar(@PathVariable Integer idSiembra, @RequestBody SiembraUpdateRequestDTO dto, Authentication authentication) {
        return ResponseEntity.ok(siembraService.actualizar(idSiembra, dto, authentication.getName()));
    }

    @DeleteMapping("/{idSiembra}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idSiembra, Authentication authentication) {
        siembraService.eliminar(idSiembra, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
