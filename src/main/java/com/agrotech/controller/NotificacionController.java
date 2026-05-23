package com.agrotech.controller;

import com.agrotech.Entity.enums.EstadoNotificacion;
import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import com.agrotech.dto.request.PreferenciaNotificacionRequestDTO;
import com.agrotech.dto.response.NotificacionResponseDTO;
import com.agrotech.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<NotificacionResponseDTO>> listar(Authentication authentication) {
        return ResponseEntity.ok(notificacionService.listar(authentication.getName()));
    }

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<NotificacionResponseDTO>> filtrarPorTipo(@PathVariable TipoNotificacion tipo, Authentication authentication) {
        return ResponseEntity.ok(notificacionService.filtrarPorTipo(authentication.getName(), tipo));
    }

    @GetMapping("/prioridad/{prioridad}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<NotificacionResponseDTO>> filtrarPorPrioridad(@PathVariable PrioridadNotificacion prioridad, Authentication authentication) {
        return ResponseEntity.ok(notificacionService.filtrarPorPrioridad(authentication.getName(), prioridad));
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<NotificacionResponseDTO>> filtrarPorEstado(@PathVariable EstadoNotificacion estado, Authentication authentication) {
        return ResponseEntity.ok(notificacionService.filtrarPorEstado(authentication.getName(), estado));
    }

    @GetMapping("/fechas")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<List<NotificacionResponseDTO>> filtrarPorFechas(@RequestParam LocalDateTime desde, @RequestParam(required = false) LocalDateTime hasta, Authentication authentication) {
        LocalDateTime fechaHasta = hasta != null ? hasta : LocalDateTime.now();
        return ResponseEntity.ok(notificacionService.filtrarPorFechas(authentication.getName(), desde, fechaHasta));
    }

    @PatchMapping("/{idNotificacion}/leer")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<NotificacionResponseDTO> marcarComoLeida(@PathVariable Integer idNotificacion, Authentication authentication) {
        return ResponseEntity.ok(notificacionService.marcarComoLeida(idNotificacion, authentication.getName()));
    }

    @PatchMapping("/leer-todas")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<Void> marcarTodasComoLeidas(Authentication authentication) {
        notificacionService.marcarTodasComoLeidas(authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/preferencias")
    @PreAuthorize("hasAnyRole('PRODUCTOR', 'OPERARIO', 'AUXILIAR')")
    public ResponseEntity<Void> configurarPreferencia(@Valid @RequestBody PreferenciaNotificacionRequestDTO dto, Authentication authentication) {
        notificacionService.configurarPreferencia(authentication.getName(), dto);
        return ResponseEntity.ok().build();
    }
}