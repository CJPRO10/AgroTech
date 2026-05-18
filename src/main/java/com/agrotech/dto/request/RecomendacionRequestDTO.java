package com.agrotech.dto.request;

public class RecomendacionRequestDTO {

    private Integer idSiembra;
    private String descripcionSolicitud;
    private String categoria;

    public RecomendacionRequestDTO() {}

    public RecomendacionRequestDTO(Integer idSiembra, String descripcionSolicitud, String categoria) {
        this.idSiembra = idSiembra;
        this.descripcionSolicitud = descripcionSolicitud;
        this.categoria = categoria;
    }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

    public String getDescripcionSolicitud() { return descripcionSolicitud; }
    public void setDescripcionSolicitud(String descripcionSolicitud) { this.descripcionSolicitud = descripcionSolicitud; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return "RecomendacionRequestDTO{" +
                "idSiembra=" + idSiembra +
                ", decripcionSolicitud='" + descripcionSolicitud + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
