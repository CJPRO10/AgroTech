// TareaRequestDTO.java
package com.agrotech.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class TareaRequestDTO {

    @NotNull(message = "El tipo de tarea es obligatorio")
    private Integer idTipoTarea;

    private String descripcion;

    @NotNull(message = "La fecha límite es obligatoria")
    private LocalDateTime fechaLimite;

    @NotNull(message = "La siembra es obligatoria")
    private Integer idSiembra;

    private Integer idOperario;
    private Integer idAuxiliar;

    public TareaRequestDTO() {}

    public Integer getIdTipoTarea() { return idTipoTarea; }
    public void setIdTipoTarea(Integer idTipoTarea) { this.idTipoTarea = idTipoTarea; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

    public Integer getIdOperario() { return idOperario; }
    public void setIdOperario(Integer idOperario) { this.idOperario = idOperario; }

    public Integer getIdAuxiliar() { return idAuxiliar; }
    public void setIdAuxiliar(Integer idAuxiliar) { this.idAuxiliar = idAuxiliar; }
}