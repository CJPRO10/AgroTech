package com.agrotech.dto.response;

import com.agrotech.Entity.enums.EstadoNotificacion;
import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import java.time.LocalDateTime;

public class NotificacionResponseDTO {

    private Integer idNotificacion;
    private String titulo;
    private String mensaje;
    private TipoNotificacion tipo;
    private PrioridadNotificacion prioridad;
    private EstadoNotificacion estado;
    private LocalDateTime fechaCreacion;
    private String nombreUsuario;

    public NotificacionResponseDTO() {}

    public Integer getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(Integer idNotificacion) { this.idNotificacion = idNotificacion; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public TipoNotificacion getTipo() { return tipo; }
    public void setTipo(TipoNotificacion tipo) { this.tipo = tipo; }

    public PrioridadNotificacion getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadNotificacion prioridad) { this.prioridad = prioridad; }

    public EstadoNotificacion getEstado() { return estado; }
    public void setEstado(EstadoNotificacion estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
}