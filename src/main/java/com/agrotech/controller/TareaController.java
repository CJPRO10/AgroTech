package com.agrotech.controller;

import com.agrotech.dto.request.ActualizarEstadoTareaRequestDTO;
import com.agrotech.dto.request.AsignarTareaRequestDTO;
import com.agrotech.dto.request.TareaRequestDTO;
import com.agrotech.dto.response.EjecucionTareaResponseDTO;
import com.agrotech.dto.response.TareaResponseDTO;
import com.agrotech.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tareaService.crear(dto, authentication.getName()));
    }

    @PostMapping("/{idTarea}/asignar")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<TareaResponseDTO> asignar(@PathVariable Integer idTarea, @RequestBody AsignarTareaRequestDTO dto, Authentication authentication) {
        return ResponseEntity.ok(tareaService.asignar(idTarea, dto, authentication.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<TareaResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(tareaService.listar(authentication.getName()));
    }

    @GetMapping("/{idTarea}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<TareaResponseDTO> buscarPorId(@PathVariable Integer idTarea, Authentication authentication) {
        return ResponseEntity.ok(tareaService.buscarPorId(idTarea, authentication.getName()));
    }

    @GetMapping("/siembra/{idSiembra}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<TareaResponseDTO>> listarPorSiembra(@PathVariable Integer idSiembra, Authentication authentication) {
        return ResponseEntity.ok(tareaService.listarPorSiembra(idSiembra, authentication.getName()));
    }

    @PatchMapping("/ejecucion/{idEjecucion}/estado")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<EjecucionTareaResponseDTO> actualizarEstado(@PathVariable Integer idEjecucion, @Valid @RequestBody ActualizarEstadoTareaRequestDTO dto, Authentication authentication) {
        return ResponseEntity.ok(tareaService.actualizarEstado(idEjecucion, dto, authentication.getName()));
    }
}
