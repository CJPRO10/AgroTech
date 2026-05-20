package com.agrotech.controller;

import com.agrotech.dto.request.LoginRequestDTO;
import com.agrotech.dto.request.RegistroProductorRequestDTO;
import com.agrotech.dto.response.LoginResponseDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;
import com.agrotech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/registro/productor")
    public ResponseEntity<UsuarioResponseDTO> registrarProductor(@Valid @RequestBody RegistroProductorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrarProductor(dto));
    }

}