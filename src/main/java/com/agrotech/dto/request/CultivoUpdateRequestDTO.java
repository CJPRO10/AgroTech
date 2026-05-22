package com.agrotech.dto.request;

public class CultivoUpdateRequestDTO {

    private String nombre;
    private Integer idTipoCultivo;

    public CultivoUpdateRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getIdTipoCultivo() { return idTipoCultivo; }
    public void setIdTipoCultivo(Integer idTipoCultivo) { this.idTipoCultivo = idTipoCultivo; }
}