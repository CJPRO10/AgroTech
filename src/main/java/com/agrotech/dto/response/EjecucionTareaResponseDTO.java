package com.agrotech.dto.response;

import com.agrotech.Entity.enums.EstadoTarea;
import java.time.LocalDateTime;

public class EjecucionTareaResponseDTO {

    private Integer idEjecucion;
    private EstadoTarea estado;
    private LocalDateTime fechaEstado;
    private LocalDateTime fechaLimite;
    private String creadoPor;
    private String operarioAsignado;
    private String auxiliarAsignado;

    public EjecucionTareaResponseDTO() {}

    public Integer getIdEjecucion() { return idEjecucion; }
    public void setIdEjecucion(Integer idEjecucion) { this.idEjecucion = idEjecucion; }

    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }

    public LocalDateTime getFechaEstado() { return fechaEstado; }
    public void setFechaEstado(LocalDateTime fechaEstado) { this.fechaEstado = fechaEstado; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }

    public String getOperarioAsignado() { return operarioAsignado; }
    public void setOperarioAsignado(String operarioAsignado) { this.operarioAsignado = operarioAsignado; }

    public String getAuxiliarAsignado() { return auxiliarAsignado; }
    public void setAuxiliarAsignado(String auxiliarAsignado) { this.auxiliarAsignado = auxiliarAsignado; }
}