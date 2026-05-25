package com.agrotech.controller;

import com.agrotech.Entity.Ubicacion;
import com.agrotech.dto.request.UbicacionRequestDTO;
import com.agrotech.service.UbicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public ResponseEntity<List<Ubicacion>> listar() {
        return ResponseEntity.ok(ubicacionService.listar());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Ubicacion>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(ubicacionService.buscarPorNombre(nombre));
    }

    @PostMapping
    public ResponseEntity<Ubicacion> crear(@RequestBody UbicacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ubicacionService.crearOReusar(dto));
    }
}