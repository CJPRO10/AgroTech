package com.agrotech.Entity;

import com.agrotech.Entity.enums.EstadoNotificacion;
import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @Column(name = "titulo", length = 250, nullable = false)
    private String titulo;

    @Column(name = "mensaje", length = 500, nullable = false)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 100, nullable = false)
    private TipoNotificacion tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", length = 50, nullable = false)
    private PrioridadNotificacion prioridad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50, nullable = false)
    private EstadoNotificacion estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clima")
    private Clima clima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anomalia")
    private Anomalia anomalia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_recomendacion")
    private Recomendacion recomendacion;

    public Notificacion() {}

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

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Clima getClima() { return clima; }
    public void setClima(Clima clima) { this.clima = clima; }

    public Anomalia getAnomalia() { return anomalia; }
    public void setAnomalia(Anomalia anomalia) { this.anomalia = anomalia; }

    public Recomendacion getRecomendacion() { return recomendacion; }
    public void setRecomendacion(Recomendacion recomendacion) { this.recomendacion = recomendacion; }
}