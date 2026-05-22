package com.agrotech.controller;

import com.agrotech.dto.request.SiembraEstadoCultivoRequestDTO;
import com.agrotech.dto.response.SiembraEstadoCultivoResponseDTO;
import com.agrotech.service.SiembraEstadoCultivoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/siembras-estados")
public class SiembraEstadoCultivoController {

    private final SiembraEstadoCultivoService siembraEstadoCultivoService;

    public SiembraEstadoCultivoController(SiembraEstadoCultivoService siembraEstadoCultivoService) {
        this.siembraEstadoCultivoService = siembraEstadoCultivoService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<SiembraEstadoCultivoResponseDTO> registrarEstadoCultivo(@Valid @RequestBody SiembraEstadoCultivoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(siembraEstadoCultivoService.registrarEstadoCultivo(request));
    }

    @GetMapping("/siembra/{idSiembra}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraEstadoCultivoResponseDTO>> listarPorSiembra(@PathVariable Integer idSiembra) {
        return ResponseEntity.ok(siembraEstadoCultivoService.listarPorSiembra(idSiembra));
    }

    @GetMapping("/siembra/{idSiembra}/actual")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<SiembraEstadoCultivoResponseDTO> obtenerEstadoActual(@PathVariable Integer idSiembra) {
        return ResponseEntity.ok(siembraEstadoCultivoService.obtenerEstadoCultivoActual(idSiembra));
    }

    @GetMapping("/estado/{idEstadoCultivo}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<SiembraEstadoCultivoResponseDTO>> listarPorEstado(@PathVariable Integer idEstadoCultivo) {
        return ResponseEntity.ok(siembraEstadoCultivoService.listarPorEstadoCultivo(idEstadoCultivo));
    }

    @DeleteMapping("/siembra/{idSiembra}/estado/{idEstadoCultivo}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<Void> eliminarEstadoCultivo(@PathVariable Integer idSiembra, @PathVariable Integer idEstadoCultivo) {
        siembraEstadoCultivoService.eliminarEstadoCultivo(idSiembra, idEstadoCultivo);
        return ResponseEntity.noContent().build();
    }
}
