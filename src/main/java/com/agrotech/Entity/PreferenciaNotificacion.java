package com.agrotech.Entity;

import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import jakarta.persistence.*;

@Entity
@Table(name = "preferencias_notificacion")
public class PreferenciaNotificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_preferencia")
    private Integer idPreferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", length = 100, nullable = false)
    private TipoNotificacion tipoAlerta;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_minimo_prioridad", length = 50)
    private PrioridadNotificacion nivelMinimoPrioridad;

    public PreferenciaNotificacion() {}

    public Integer getIdPreferencia() { return idPreferencia; }
    public void setIdPreferencia(Integer idPreferencia) { this.idPreferencia = idPreferencia; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public TipoNotificacion getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(TipoNotificacion tipoAlerta) { this.tipoAlerta = tipoAlerta; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public PrioridadNotificacion getNivelMinimoPrioridad() { return nivelMinimoPrioridad; }
    public void setNivelMinimoPrioridad(PrioridadNotificacion nivelMinimoPrioridad) { this.nivelMinimoPrioridad = nivelMinimoPrioridad; }
}