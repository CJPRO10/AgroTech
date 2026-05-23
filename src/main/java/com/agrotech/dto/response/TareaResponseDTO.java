// TareaResponseDTO.java
package com.agrotech.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TareaResponseDTO {

    private Integer idTarea;
    private String tipoTarea;
    private String descripcion;
    private LocalDateTime fechaLimite;
    private Integer idSiembra;
    private String nombreSiembra;
    private List<EjecucionTareaResponseDTO> asignaciones;

    public TareaResponseDTO() {}

    public Integer getIdTarea() { return idTarea; }
    public void setIdTarea(Integer idTarea) { this.idTarea = idTarea; }

    public String getTipoTarea() { return tipoTarea; }
    public void setTipoTarea(String tipoTarea) { this.tipoTarea = tipoTarea; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

    public String getNombreSiembra() { return nombreSiembra; }
    public void setNombreSiembra(String nombreSiembra) { this.nombreSiembra = nombreSiembra; }

    public List<EjecucionTareaResponseDTO> getAsignaciones() { return asignaciones; }
    public void setAsignaciones(List<EjecucionTareaResponseDTO> asignaciones) { this.asignaciones = asignaciones; }
}