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
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<RecomendacionResponseDTO> solicitar(@RequestBody RecomendacionRequestDTO dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recomendacionService.solicitar(dto));
    }

    @GetMapping
    public ResponseEntity<List<RecomendacionResponseDTO>> listar() {
        return ResponseEntity.ok(recomendacionService.listar());
    }

    @GetMapping("/{idRecomendacion}")
    public ResponseEntity<RecomendacionResponseDTO> buscarPorId(@PathVariable Integer idRecomendacion) {
        return ResponseEntity.ok(recomendacionService.buscarPorId(idRecomendacion));
    }

    @GetMapping("/siembra/{idSiembra}")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorSiembra(@PathVariable Integer idSiembra) {
        return ResponseEntity.ok(recomendacionService.listarPorSiembra(idSiembra));
    }

    @GetMapping("/anomalia/{idAnomalia}")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorAnomalia(@PathVariable Integer idAnomalia) {
        return ResponseEntity.ok(recomendacionService.listarPorAnomalia(idAnomalia));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(recomendacionService.listarPorCategoria(categoria));
    }

    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorPrioridad(@PathVariable PrioridadRecomendacion prioridad) {
        return ResponseEntity.ok(recomendacionService.listarPorPrioridad(prioridad));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorEstado(@PathVariable EstadoRecomendacion estado) {
        return ResponseEntity.ok(recomendacionService.listarPorEstado(estado));
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<RecomendacionResponseDTO>> listarPorRangoFechas(@RequestParam LocalDateTime desde, @RequestParam(required = false) LocalDateTime hasta) {
        LocalDateTime fechaHasta = hasta != null ? hasta : LocalDateTime.now();
        return ResponseEntity.ok(recomendacionService.listarPorRangoFechas(desde, fechaHasta));
    }

    @PatchMapping("/{idRecomendacion}/ignorar")
    public ResponseEntity<RecomendacionResponseDTO> ignorar(@PathVariable Integer idRecomendacion) {
        return ResponseEntity.ok(recomendacionService.ignorar(idRecomendacion));
    }

    @PatchMapping("/{idRecomendacion}/reaccionar")
    public ResponseEntity<RecomendacionResponseDTO> reaccionar(@PathVariable Integer idRecomendacion, @RequestParam String reaccion) {
        return ResponseEntity.ok(recomendacionService.reaccionar(idRecomendacion, reaccion));
    }
}
