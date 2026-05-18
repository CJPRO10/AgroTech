package com.agrotech.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PronosticoResponseDTO {

    private String nombreUbicacion;
    private Integer idUbicacion;
    private List<DiasPronostico> dias;

    public PronosticoResponseDTO() {}

    public PronosticoResponseDTO(String nombreUbicacion, Integer idUbicacion, List<DiasPronostico> dias) {
        this.nombreUbicacion = nombreUbicacion;
        this.idUbicacion = idUbicacion;
        this.dias = dias;
    }

    public String getNombreUbicacion() { return nombreUbicacion; }
    public void setNombreUbicacion(String nombreUbicacion) { this.nombreUbicacion = nombreUbicacion; }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }

    public List<DiasPronostico> getDias() { return dias; }
    public void setDias(List<DiasPronostico> dias) { this.dias = dias; }

    public static class DiasPronostico {

        private LocalDate fecha;
        private BigDecimal temperaturaMaxima;
        private BigDecimal temperaturaMinima;
        private Float precipitacion;
        private String condicion;
        private String alerta;

        public DiasPronostico() {}

        public DiasPronostico(LocalDate fecha, BigDecimal temperaturaMaxima,
                              BigDecimal temperaturaMinima, Float precipitacion,
                              String condicion, String alerta) {
            this.fecha = fecha;
            this.temperaturaMaxima = temperaturaMaxima;
            this.temperaturaMinima = temperaturaMinima;
            this.precipitacion = precipitacion;
            this.condicion = condicion;
            this.alerta = alerta;
        }

        public LocalDate getFecha() { return fecha; }
        public void setFecha(LocalDate fecha) { this.fecha = fecha; }

        public BigDecimal getTemperaturaMaxima() { return temperaturaMaxima; }
        public void setTemperaturaMaxima(BigDecimal temperaturaMaxima) { this.temperaturaMaxima = temperaturaMaxima; }

        public BigDecimal getTemperaturaMinima() { return temperaturaMinima; }
        public void setTemperaturaMinima(BigDecimal temperaturaMinima) { this.temperaturaMinima = temperaturaMinima; }

        public Float getPrecipitacion() { return precipitacion; }
        public void setPrecipitacion(Float precipitacion) { this.precipitacion = precipitacion; }

        public String getCondicion() { return condicion; }
        public void setCondicion(String condicion) { this.condicion = condicion; }

        public String getAlerta() { return alerta; }
        public void setAlerta(String alerta) { this.alerta = alerta; }
    }
}