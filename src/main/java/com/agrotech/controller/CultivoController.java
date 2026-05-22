package com.agrotech.controller;

import com.agrotech.dto.request.CultivoRequestDTO;
import com.agrotech.dto.request.CultivoUpdateRequestDTO;
import com.agrotech.dto.response.CultivoResponseDTO;
import com.agrotech.service.CultivoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cultivos")
public class CultivoController {

    private final CultivoService cultivoService;

    public CultivoController(CultivoService cultivoService) {
        this.cultivoService = cultivoService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CultivoResponseDTO> crearCultivo(@Valid @RequestBody CultivoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cultivoService.crear(dto));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CultivoResponseDTO>> listarCultivos() {
        return ResponseEntity.ok(cultivoService.listarTodos());
    }

    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CultivoResponseDTO>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(cultivoService.buscarPorNombre(nombre));
    }

    @GetMapping("/tipo/{idTipoCultivo}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CultivoResponseDTO>> buscarPorTipo(@PathVariable Integer idTipoCultivo) {
        return ResponseEntity.ok(cultivoService.buscarPorTipo(idTipoCultivo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CultivoResponseDTO> actualizarCultivo(@PathVariable Integer id, @RequestBody CultivoUpdateRequestDTO dto) {
        return ResponseEntity.ok(cultivoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminarCultivo(@PathVariable Integer id) {
        cultivoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
