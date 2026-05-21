package com.agrotech.controller;

import com.agrotech.dto.request.EditarPerfilRequestDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;
import com.agrotech.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
public class PerfilController {

    private final UsuarioService usuarioService;

    public PerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<UsuarioResponseDTO> verPerfil(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(usuarioService.verPerfil(correo));
    }

    @PutMapping
    public ResponseEntity<UsuarioResponseDTO> editarPerfil(Authentication authentication, @Valid @RequestBody EditarPerfilRequestDTO dto) {
        String correo = authentication.getName();
        return ResponseEntity.ok(usuarioService.editarPerfil(correo, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarPerfil(Authentication authentication) {
        String correo = authentication.getName();
        usuarioService.eliminarPerfil(correo);
        return ResponseEntity.noContent().build();
    }
}