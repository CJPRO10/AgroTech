package com.agrotech.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ── Autenticación ─────────────────────────────────────────────────────────

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabled(DisabledException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "La cuenta está desactivada. Contacta al administrador.");
    }

    // ── Validación de campos (@Valid en DTOs) ─────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        // Recopilar todos los errores de campo
        Map<String, String> camposConError = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = ((FieldError) error).getField();
            String mensaje = error.getDefaultMessage();
            camposConError.put(campo, mensaje);
        });

        // Mensaje principal con el primer error
        String mensajePrincipal = camposConError.values().stream().findFirst()
                .orElse("Hay campos con errores de validación");

        // Si hay múltiples errores, listarlos
        if (camposConError.size() > 1) {
            mensajePrincipal = camposConError.values().stream()
                    .collect(Collectors.joining(". "));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", mensajePrincipal);
        body.put("campos", camposConError);
        return ResponseEntity.badRequest().body(body);
    }

    // ── Parámetros faltantes ──────────────────────────────────────────────────

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return buildError(HttpStatus.BAD_REQUEST,
                "Parámetro requerido faltante: " + ex.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildError(HttpStatus.BAD_REQUEST,
                "Valor inválido para el campo '" + ex.getName() + "'");
    }

    // ── Violación de restricciones de BD ─────────────────────────────────────

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String mensaje = "Error de integridad de datos";
        String causa = ex.getRootCause() != null ? ex.getRootCause().getMessage() : "";

        if (causa.contains("correo") || causa.contains("email")) {
            mensaje = "Ya existe una cuenta con ese correo electrónico";
        } else if (causa.contains("nombre_finca") || causa.contains("nombreFinca")) {
            mensaje = "Ya tienes una finca con ese nombre";
        } else if (causa.contains("unique") || causa.contains("duplicate")) {
            mensaje = "Ya existe un registro con esos datos";
        }

        return buildError(HttpStatus.CONFLICT, mensaje);
    }

    // ── Errores de negocio (RuntimeException) ────────────────────────────────

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        String mensaje = ex.getMessage();
        if (mensaje == null || mensaje.isBlank()) {
            mensaje = "Ha ocurrido un error inesperado";
        }
        return buildError(HttpStatus.BAD_REQUEST, mensaje);
    }

    // ── Error genérico ────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor. Intenta nuevamente.");
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String mensaje) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}