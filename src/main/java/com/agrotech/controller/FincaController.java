package com.agrotech.controller;

import com.agrotech.dto.request.FincaRequestDTO;
import com.agrotech.dto.request.FincaUpdateRequestDTO;
import com.agrotech.dto.response.FincaResponseDTO;
import com.agrotech.service.FincaServIce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fincas")
public class FincaController {

    private final FincaServIce fincaService;

    public FincaController(FincaServIce fincaService) {
        this.fincaService = fincaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<FincaResponseDTO>> listarMisFincas(Authentication authentication) {
        return ResponseEntity.ok(fincaService.listarPorCorreo(authentication.getName()));
    }

    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<List<FincaResponseDTO>> buscarPorNombre(@PathVariable String nombre, Authentication authentication) {
        return ResponseEntity.ok(fincaService.buscarPorNombre(nombre, authentication.getName()));
    }

    @PostMapping
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<FincaResponseDTO> crear(@RequestBody FincaRequestDTO dto, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fincaService.crear(authentication.getName(), dto));
    }

    @PutMapping("/{idFinca}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<FincaResponseDTO> actualizar(@PathVariable Integer idFinca, @RequestBody FincaUpdateRequestDTO dto, Authentication authentication) {
        return ResponseEntity.ok(fincaService.actualizar(idFinca, authentication.getName(), dto));
    }

    @DeleteMapping("/{idFinca}")
    @PreAuthorize("hasRole('PRODUCTOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idFinca, Authentication authentication) {
        fincaService.eliminar(idFinca, authentication.getName());
        return ResponseEntity.noContent().build();
    }

}
