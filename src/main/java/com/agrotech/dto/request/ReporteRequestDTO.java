package com.agrotech.dto.request;

public class ReporteRequestDTO {

    private String nombreReporte;
    private String formato;
    private String tipoPeriodicidad;

    public ReporteRequestDTO() {}

    public String getNombreReporte() { return nombreReporte; }
    public void setNombreReporte(String nombreReporte) { this.nombreReporte = nombreReporte; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getTipoPeriodicidad() { return tipoPeriodicidad; }
    public void setTipoPeriodicidad(String tipoPeriodicidad) { this.tipoPeriodicidad = tipoPeriodicidad; }
}
