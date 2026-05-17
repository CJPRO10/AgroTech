package com.agrotech.controller;

import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;
import com.agrotech.dto.request.AnomaliaRequestDTO;
import com.agrotech.dto.response.AnomaliaResponseDTO;
import com.agrotech.service.AnomaliaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Resgistrar anomalia
    @PostMapping
    public ResponseEntity<AnomaliaResponseDTO> registrar(@RequestBody AnomaliaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anomaliaService.registrar(dto));
    }

    // Consultar todas
    @GetMapping
    public ResponseEntity<List<AnomaliaResponseDTO>> listar() {
        return ResponseEntity.ok(anomaliaService.listar());
    }

    // Consultar por id
    @GetMapping("/{idAnomalia}")
    public ResponseEntity<AnomaliaResponseDTO> buscarPorId(@PathVariable Integer idAnomalia) {
        return ResponseEntity.ok(anomaliaService.buscarPorId(idAnomalia));
    }

    // Consultar por siembra
    @GetMapping("/siembra/{idSiembra}")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorSiembra(@PathVariable Integer idSiembra) {
        return ResponseEntity.ok(anomaliaService.listarPorSiembra(idSiembra));
    }

    // Filtrar por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorTipo(@PathVariable TipoAnomalia tipo) {
        return ResponseEntity.ok(anomaliaService.listarPorTipo(tipo));
    }

    // Filtrar por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorEstado(@PathVariable EstadoAnomalia estado) {
        return ResponseEntity.ok(anomaliaService.listarPorEstado(estado));
    }

    // Filtrar por nivel de severidad
    @GetMapping("/severidad/{nivelSeveridad}")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorNivelSeveridad(@PathVariable NivelSeveridad nivelSeveridad) {
        return ResponseEntity.ok(anomaliaService.listarPorNivelSeveridad(nivelSeveridad));
    }

    // Filtrar por rango de fechas
    @GetMapping("/fechas")
    public ResponseEntity<List<AnomaliaResponseDTO>> listarPorRangoFechas(
            @RequestParam LocalDateTime desde,
            @RequestParam(required = false) LocalDateTime hasta) {
        LocalDateTime fechaHasta = hasta != null ? hasta : LocalDateTime.now();
        return ResponseEntity.ok(anomaliaService.listarPorRangoFechas(desde, fechaHasta));
    }

    // Actualizar anomalia
    @PutMapping("/{idAnomalia}")
    public ResponseEntity<AnomaliaResponseDTO> actualizar(
            @PathVariable Integer idAnomalia,
            @RequestBody AnomaliaRequestDTO dto) {
        return ResponseEntity.ok(anomaliaService.actualizar(idAnomalia, dto));
    }

    // Eliminar anomalia
    @DeleteMapping("/{idAnomalia}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idAnomalia) {
        anomaliaService.eliminar(idAnomalia);
        return ResponseEntity.noContent().build();
    }
}
