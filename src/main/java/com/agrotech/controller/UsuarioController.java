package com.agrotech.controller;

import com.agrotech.dto.request.UsuarioRequestDTO;
import com.agrotech.dto.request.UsuarioUpdateRequestDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;
import com.agrotech.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PRODUCTOR')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto, Authentication authentication) {
        String correoSolicitante = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crear(dto, correoSolicitante));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PRODUCTOR')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(Authentication authentication) {
        String correo = authentication.getName();
        return ResponseEntity.ok(usuarioService.listar(correo));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PRODUCTOR')")
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNombre(@PathVariable String nombre, Authentication authentication) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre, authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PRODUCTOR')")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorCorreo(@PathVariable String correo, Authentication authentication) {
        return ResponseEntity.ok(usuarioService.buscarPorCorreo(correo, authentication.getName()));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PRODUCTOR')")
    @GetMapping("/rol/{nombreRol}")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorRol(@PathVariable String nombreRol, Authentication authentication) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(nombreRol, authentication.getName()));
    }

    @PutMapping("/{idUsuario}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> actualizar(@PathVariable Integer idUsuario, @Valid @RequestBody UsuarioUpdateRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.actualizar(idUsuario, dto));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idUsuario) {
        usuarioService.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idUsuario}/desactivar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PRODUCTOR')")
    public ResponseEntity<UsuarioResponseDTO> desactivar(@PathVariable Integer idUsuario, Authentication authentication) {
        return ResponseEntity.ok(usuarioService.desactivar(idUsuario, authentication.getName()));
    }

}