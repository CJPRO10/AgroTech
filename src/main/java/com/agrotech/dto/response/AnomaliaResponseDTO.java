package com.agrotech.dto.response;

import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;

import java.time.LocalDateTime;

public class AnomaliaResponseDTO {

    private Integer idAnomalia;
    private String nombre;
    private TipoAnomalia tipo;
    private EstadoAnomalia estado;
    private String descripcion;
    private NivelSeveridad nivelSeveridad;
    private LocalDateTime fechaDeteccion;
    private Integer idSiembra;
    private String nombreCultivo;
    private String nombreFinca;
    private RecomendacionResponseDTO recomendacion;

    public AnomaliaResponseDTO() {}

    public AnomaliaResponseDTO(Integer idAnomalia, String nombre, TipoAnomalia tipo, EstadoAnomalia estado,
                               String descripcion, NivelSeveridad nivelSeveridad, LocalDateTime fechaDeteccion,
                               Integer idSiembra, String nombreCultivo, String nombreFinca, RecomendacionResponseDTO recomendacion) {
        this.idAnomalia = idAnomalia;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.nivelSeveridad = nivelSeveridad;
        this.fechaDeteccion = fechaDeteccion;
        this.idSiembra = idSiembra;
        this.nombreCultivo = nombreCultivo;
        this.nombreFinca = nombreFinca;
        this.recomendacion = recomendacion;
    }

    public Integer getIdAnomalia() { return idAnomalia; }
    public void setIdAnomalia(Integer idAnomalia) { this.idAnomalia = idAnomalia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoAnomalia getTipo() { return tipo; }
    public void setTipo(TipoAnomalia tipo) { this.tipo = tipo; }

    public EstadoAnomalia getEstado() { return estado; }
    public void setEstado(EstadoAnomalia estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public NivelSeveridad getNivelSeveridad() { return nivelSeveridad; }
    public void setNivelSeveridad(NivelSeveridad nivelSeveridad) { this.nivelSeveridad = nivelSeveridad; }

    public LocalDateTime getFechaDeteccion() { return fechaDeteccion; }
    public void setFechaDeteccion(LocalDateTime fechaDeteccion) { this.fechaDeteccion = fechaDeteccion; }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

    public String getNombreCultivo() { return nombreCultivo;}
    public void setNombreCultivo(String nombreCultivo) { this.nombreCultivo = nombreCultivo; }

    public String getNombreFinca() { return nombreFinca; }
    public void setNombreFinca(String nombreFinca) { this.nombreFinca = nombreFinca; }

    public RecomendacionResponseDTO getRecomendacion() { return recomendacion; }
    public void setRecomendacion(RecomendacionResponseDTO recomendacion) { this.recomendacion = recomendacion; }

    @Override
    public String toString() {
        return "AnomaliaResponseDTO{" +
                "idAnomalia=" + idAnomalia +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nivelSeveridad='" + nivelSeveridad + '\'' +
                ", fechaDeteccion=" + fechaDeteccion +
                ", idSiembra=" + idSiembra +
                ", nombreCultivo='" + nombreCultivo + '\'' +
                ", nombreFinca='" + nombreFinca + '\'' +
                ", recomendacion=" + recomendacion +
                '}';
    }
}
