package com.agrotech.dto.response;

public class FincaResponseDTO {

    private Integer idFinca;
    private String nombreFinca;
    private Double hectareas;
    private String nombreUbicacion;
    private Integer idUbicacion;
    private Integer numLotes;

    public FincaResponseDTO() {}

    public FincaResponseDTO(Integer idFinca, String nombreFinca, Double hectareas, String nombreUbicacion, Integer idUbicacion, Integer numLotes) {
        this.idFinca = idFinca;
        this.nombreFinca = nombreFinca;
        this.hectareas = hectareas;
        this.nombreUbicacion = nombreUbicacion;
        this.idUbicacion = idUbicacion;
        this.numLotes = numLotes;
    }

    public Integer getIdFinca() {
        return idFinca;
    }

    public void setIdFinca(Integer idFinca) {
        this.idFinca = idFinca;
    }

    public String getNombreFinca() {
        return nombreFinca;
    }

    public void setNombreFinca(String nombreFinca) {
        this.nombreFinca = nombreFinca;
    }

    public Double getHectareas() {
        return hectareas;
    }

    public void setHectareas(Double hectareas) {
        this.hectareas = hectareas;
    }

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }

    public Integer getIdUbicacion() {
        return idUbicacion;
    }
    public void setIdUbicacion(Integer idUbicacion) {this.idUbicacion = idUbicacion;}

    public Integer getNumLotes() {
        return numLotes;
    }

    public void setNumLotes(Integer numLotes) {
        this.numLotes = numLotes;
    }

    @Override
    public String toString() {
        return "FincaResponse{" +
                "idFinca=" + idFinca +
                ", nombreFinca='" + nombreFinca + '\'' +
                ", hectareas=" + hectareas +
                ", nombreUbicacion='" + nombreUbicacion + '\'' +
                ", idUbicacion=" + idUbicacion +
                "numLotes=" + numLotes + '}';
    }
}
