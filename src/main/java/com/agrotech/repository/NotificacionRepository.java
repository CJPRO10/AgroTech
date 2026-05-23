package com.agrotech.repository;

import com.agrotech.Entity.Notificacion;
import com.agrotech.Entity.enums.EstadoNotificacion;
import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuario_IdUsuarioOrderByFechaCreacionDesc(Integer idUsuario);
    List<Notificacion> findByUsuario_IdUsuarioAndTipo(Integer idUsuario, TipoNotificacion tipo);
    List<Notificacion> findByUsuario_IdUsuarioAndPrioridad(Integer idUsuario, PrioridadNotificacion prioridad);
    List<Notificacion> findByUsuario_IdUsuarioAndEstado(Integer idUsuario, EstadoNotificacion estado);
    List<Notificacion> findByUsuario_IdUsuarioAndFechaCreacionBetween(Integer idUsuario, LocalDateTime desde, LocalDateTime hasta);
}