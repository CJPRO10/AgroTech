package com.agrotech.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class FincaUpdateRequestDTO {

    @NotBlank(message = "El nombre de la finca es obligatorio")
    private String nombreFinca;

    @Positive(message = "Las hectáreas deben ser un número positivo")
    private Double hectareas;

    private Integer numLotes;

    private Integer idUbicacion;

    public FincaUpdateRequestDTO() {}

    public FincaUpdateRequestDTO(String nombreFinca, Double hectareas, Integer numLotes, Integer idUbicacion) {
        this.nombreFinca = nombreFinca;
        this.hectareas = hectareas;
        this.numLotes = numLotes;
        this.idUbicacion = idUbicacion;
    }

    public void setNombreFinca(String nombreFinca) {
        this.nombreFinca = nombreFinca;
    }

    public String getNombreFinca() {
        return nombreFinca;
    }

    public void setHectareas(Double hectareas) {
        this.hectareas = hectareas;
    }

    public Double getHectareas() {
        return hectareas;
    }

    public Integer getNumLotes() {
        return numLotes;
    }
    public void setNumLotes(Integer numLotes) {this.numLotes = numLotes;}

    public Integer getIdUbicacion() {
        return idUbicacion;
    }
    public void setIdUbicacion(Integer idUbicacion) {this.idUbicacion = idUbicacion;}
    @Override
    public String toString() {
        return "FincaUpdateRequest{" +
                "nombreFinca='" + nombreFinca + '\'' +
                ", hectareas=" + hectareas +
                ", numLotes=" + numLotes +
                ", idUbicacion=" + idUbicacion +
                '}';
    }
}
