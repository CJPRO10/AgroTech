package com.agrotech.dto.request;

import com.agrotech.Entity.enums.EstadoTarea;
import jakarta.validation.constraints.NotNull;

public class ActualizarEstadoTareaRequestDTO {

    @NotNull(message = "El estado es obligatorio")
    private EstadoTarea estado;

    public ActualizarEstadoTareaRequestDTO() {}

    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }
}