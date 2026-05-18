package com.agrotech.dto.response;

import java.time.LocalDateTime;

public class RecomendacionResponseDTO {

    private Integer idRecomendacion;
    private String descripcion;
    private String categoria;
    private String prioridad;
    private String estado;
    private LocalDateTime fechaGeneracion;
    private Integer idSiembra;
    private Integer idAnomalia;
    private Integer idClima;

    public RecomendacionResponseDTO() {}

    public RecomendacionResponseDTO(Integer idRecomendacion, String descripcion, String categoria,
                                   String prioridad, String estado, LocalDateTime fechaGeneracion,
                                   Integer idSiembra, Integer idAnomalia, Integer idClima) {
        this.idRecomendacion = idRecomendacion;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaGeneracion = fechaGeneracion;
        this.idSiembra = idSiembra;
        this.idAnomalia = idAnomalia;
        this.idClima = idClima;
    }

    public Integer getIdRecomendacion() { return idRecomendacion; }
    public void setIdRecomendacion(Integer idRecomendacion) { this.idRecomendacion = idRecomendacion; }

    public String getDescripcion() { return descripcion;}
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

    public Integer getIdAnomalia() { return idAnomalia; }
    public void setIdAnomalia(Integer idAnomalia) { this.idAnomalia = idAnomalia; }

    public Integer getIdClima() { return idClima; }
    public void setIdClima(Integer idClima) { this.idClima = idClima; }

    @Override
    public String toString() {
        return "RecomendacionResponseDTO{" +
                "idRecomendacion=" + idRecomendacion +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaGeneracion=" + fechaGeneracion +
                ", idSiembra=" + idSiembra +
                ", idAnomalia=" + idAnomalia +
                ", idClima=" + idClima +
                '}';
    }
}
