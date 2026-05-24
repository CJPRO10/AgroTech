package com.agrotech.Entity;

import jakarta.persistence.*;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "climas")
public class Clima {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clima")
    private Integer idClima;

    @Column(name = "precipitacion")
    private Float precipitacion;

    @Column(name = "condicion", length = 50)
    private String condicion;

    @Column(name = "temperatura", precision = 10, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "fecha_medicion")
    private LocalDate fechaMedicion;

    @Column(name = "fecha_registro")
    private LocalDate fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;

    @OneToMany(mappedBy = "clima", fetch = FetchType.LAZY)
    private List<Recomendacion> recomendaciones;

    @OneToMany(mappedBy = "clima", fetch = FetchType.LAZY)
    private List<Notificacion> notificaciones;

    public Clima() {}

    public Clima(Integer idClima, Float precipitacion, String condicion,
                 java.math.BigDecimal temperatura, LocalDate fechaMedicion,
                 LocalDate fechaRegistro, Ubicacion ubicacion) {
        this.idClima = idClima;
        this.precipitacion = precipitacion;
        this.condicion = condicion;
        this.temperatura = temperatura;
        this.fechaMedicion = fechaMedicion;
        this.fechaRegistro = fechaRegistro;
        this.ubicacion = ubicacion;
    }

    public Integer getIdClima() { return idClima; }
    public void setIdClima(Integer idClima) { this.idClima = idClima; }

    public Float getPrecipitacion() { return precipitacion; }
    public void setPrecipitacion(Float precipitacion) { this.precipitacion = precipitacion; }

    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }

    public java.math.BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(java.math.BigDecimal temperatura) { this.temperatura = temperatura; }

    public LocalDate getFechaMedicion() { return fechaMedicion; }
    public void setFechaMedicion(LocalDate fechaMedicion) { this.fechaMedicion = fechaMedicion; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    public List<Recomendacion> getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(List<Recomendacion> recomendaciones) { this.recomendaciones = recomendaciones; }

    public List<Notificacion> getNotificaciones() { return notificaciones; }
    public void setNotificaciones(List<Notificacion> notificaciones) { this.notificaciones = notificaciones; }

    @Override
    public String toString() {
        return "Clima{idClima=" + idClima + ", condicion='" + condicion +
                "', temperatura=" + temperatura + ", fechaMedicion=" + fechaMedicion + "}";
    }

}
