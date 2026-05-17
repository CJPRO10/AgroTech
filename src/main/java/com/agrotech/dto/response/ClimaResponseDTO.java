package com.agrotech.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClimaResponseDTO {

    private Integer idClima;
    private String condicion;
    private BigDecimal temperatura;
    private Float precipitacion;
    private LocalDate fechaMedicion;
    private String nombreUbicacion;
    private Integer idUbicacion;
    private String alerta;

    public ClimaResponseDTO() {}

    public ClimaResponseDTO(Integer idClima, String condicion, BigDecimal temperatura,
                            Float precipitacion, LocalDate fechaMedicion,
                            String nombreUbicacion, Integer idUbicacion, String alerta) {
        this.idClima = idClima;
        this.condicion = condicion;
        this.temperatura = temperatura;
        this.precipitacion = precipitacion;
        this.fechaMedicion = fechaMedicion;
        this.nombreUbicacion = nombreUbicacion;
        this.idUbicacion = idUbicacion;
        this.alerta = alerta;
    }

    public Integer getIdClima() { return idClima; }
    public void setIdClima(Integer idClima) { this.idClima = idClima; }

    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }

    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }

    public Float getPrecipitacion() { return precipitacion; }
    public void setPrecipitacion(Float precipitacion) { this.precipitacion = precipitacion; }

    public LocalDate getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(LocalDate fechaMedicion) { this.fechaMedicion = fechaMedicion; }

    public String getNombreUbicacion() { return nombreUbicacion; }
    public void setNombreUbicacion(String nombreUbicacion) { this.nombreUbicacion = nombreUbicacion; }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }

    public String getAlerta() { return alerta; }
    public void setAlerta(String alerta) { this.alerta = alerta; }

    @Override
    public String toString() {
        return "ClimaResponseDTO{" +
                "condicion='" + condicion + '\'' +
                ", temperatura=" + temperatura +
                ", precipitacion=" + precipitacion +
                ", fechaMedicion=" + fechaMedicion +
                ", nombreUbicacion='" + nombreUbicacion + '\'' +
                ", alerta='" + alerta + '\'' +
                '}';
    }
}