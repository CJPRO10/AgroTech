package com.agrotech.controller;

import com.agrotech.Entity.enums.EstadoRecomendacion;
import com.agrotech.Entity.enums.PrioridadRecomendacion;
import com.agrotech.dto.request.RecomendacionRequestDTO;
import com.agrotech.dto.response.RecomendacionResponseDTO;
import com.agrotech.service.RecomendacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    public RecomendacionController(RecomendacionService recomendacionService) {
        this.recomendacionService = recomendacionService;
    }

    @PostMapping("/solicitar")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<RecomendacionResponseDTO> solicitar(@RequestBody RecomendacionRequestDTO dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recomendacionService.solicitar(dto));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.listar(authentication.getName()));
    }

    @GetMapping("/{idRecomendacion}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<RecomendacionResponseDTO> buscarPorId(@PathVariable Integer idRecomendacion, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.buscarPorId(idRecomendacion));
    }


    @GetMapping("/siembra/{idSiembra}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorSiembra(@PathVariable Integer idSiembra, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.listarPorSiembra(idSiembra));
    }

    @GetMapping("/anomalia/{idAnomalia}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorAnomalia(@PathVariable Integer idAnomalia, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.listarPorAnomalia(idAnomalia));
    }


    @GetMapping("/categoria/{categoria}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorCategoria(@PathVariable String categoria, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.listarPorCategoria(categoria));
    }

    @GetMapping("/prioridad/{prioridad}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorPrioridad(@PathVariable PrioridadRecomendacion prioridad, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.listarPorPrioridad(prioridad));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorEstado(@PathVariable EstadoRecomendacion estado, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.listarPorEstado(estado));
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorFechas(@RequestParam LocalDateTime desde, @RequestParam(required = false) LocalDateTime hasta, Authentication authentication) {
        LocalDateTime fechaHasta = hasta != null ? hasta : LocalDateTime.now();
        return ResponseEntity.ok(recomendacionService.listarPorRangoFechas(desde, fechaHasta));
    }

    @PatchMapping("/{idRecomendacion}/ignorar")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<RecomendacionResponseDTO> ignorar(@PathVariable Integer idRecomendacion, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.ignorar(idRecomendacion));
    }

    @PatchMapping("/{idRecomendacion}/reaccionar")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO')")
    public ResponseEntity<RecomendacionResponseDTO> reaccionar(@PathVariable Integer idRecomendacion, @RequestParam String reaccion, Authentication authentication) {
        return ResponseEntity.ok(recomendacionService.reaccionar(idRecomendacion, reaccion));
    }
}