package com.agrotech.controller;

import com.agrotech.Entity.EstadoCultivo;
import com.agrotech.repository.EstadoCultivoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estado-cultivo")
public class EstadoCultivoController {

    private final EstadoCultivoRepository estadoCultivoRepository;

    public EstadoCultivoController(EstadoCultivoRepository estadoCultivoRepository) {
        this.estadoCultivoRepository = estadoCultivoRepository;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EstadoCultivo>> listar() {
        return ResponseEntity.ok(estadoCultivoRepository.findAll());
    }
}
