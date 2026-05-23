package com.agrotech.service;

import com.agrotech.Entity.Anomalia;
import com.agrotech.Entity.Clima;
import com.agrotech.Entity.Recomendacion;
import com.agrotech.dto.request.PreferenciaNotificacionRequestDTO;
import com.agrotech.dto.response.NotificacionResponseDTO;
import com.agrotech.Entity.enums.EstadoNotificacion;
import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificacionService {

    List<NotificacionResponseDTO> listar(String correo);
    List<NotificacionResponseDTO> filtrarPorTipo(String correo, TipoNotificacion tipo);
    List<NotificacionResponseDTO> filtrarPorPrioridad(String correo, PrioridadNotificacion prioridad);
    List<NotificacionResponseDTO> filtrarPorEstado(String correo, EstadoNotificacion estado);
    List<NotificacionResponseDTO> filtrarPorFechas(String correo, LocalDateTime desde, LocalDateTime hasta);
    NotificacionResponseDTO marcarComoLeida(Integer idNotificacion, String correo);
    void marcarTodasComoLeidas(String correo);
    void generarNotificacionAnomalia(Anomalia anomalia);
    void generarNotificacionRecomendacion(Recomendacion recomendacion);
    void generarNotificacionClima(Clima clima, String mensaje);
    List<NotificacionResponseDTO> obtenerPreferencias(String correo);
    void configurarPreferencia(String correo, PreferenciaNotificacionRequestDTO dto);
}