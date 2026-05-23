package com.agrotech.controller;

import com.agrotech.Entity.TipoTarea;
import com.agrotech.repository.TipoTareaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-tarea")
public class TipoTareaController {

    private final TipoTareaRepository tipoTareaRepository;

    public TipoTareaController(TipoTareaRepository tipoTareaRepository) {
        this.tipoTareaRepository = tipoTareaRepository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TipoTarea>> listar() {
        return ResponseEntity.ok(tipoTareaRepository.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoTarea> crear(@RequestBody TipoTarea tipoTarea) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoTareaRepository.save(tipoTarea));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<TipoTarea> actualizar(@PathVariable Integer id, @RequestBody TipoTarea dto) {
        TipoTarea tipo = tipoTareaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de tarea no encontrado: " + id));
        tipo.setNombre(dto.getNombre());
        tipo.setDescripcion(dto.getDescripcion());
        return ResponseEntity.ok(tipoTareaRepository.save(tipo));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoTareaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}