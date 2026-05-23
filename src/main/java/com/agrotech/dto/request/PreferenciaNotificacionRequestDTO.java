package com.agrotech.dto.request;

import com.agrotech.Entity.enums.PrioridadNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import jakarta.validation.constraints.NotNull;

public class PreferenciaNotificacionRequestDTO {

    @NotNull(message = "El tipo de alerta es obligatorio")
    private TipoNotificacion tipoAlerta;

    private boolean activo = true;

    private PrioridadNotificacion nivelMinimoPrioridad;

    public PreferenciaNotificacionRequestDTO() {}

    public TipoNotificacion getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(TipoNotificacion tipoAlerta) { this.tipoAlerta = tipoAlerta; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public PrioridadNotificacion getNivelMinimoPrioridad() { return nivelMinimoPrioridad; }
    public void setNivelMinimoPrioridad(PrioridadNotificacion nivelMinimoPrioridad) { this.nivelMinimoPrioridad = nivelMinimoPrioridad; }
}