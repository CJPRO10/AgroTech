package com.agrotech.dto.request;

public class UbicacionRequestDTO {

    private String nombre;
    private Double latitud;
    private Double longitud;

    public UbicacionRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
}