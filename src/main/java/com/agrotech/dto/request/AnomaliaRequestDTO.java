package com.agrotech.dto.request;

import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;

import java.time.LocalDateTime;

public class AnomaliaRequestDTO {

    private Integer idSiembra;
    private String nombre;
    private TipoAnomalia tipo;
    private EstadoAnomalia estado;
    private String descripcion;
    private NivelSeveridad nivelSeveridad;
    private LocalDateTime fechaDeteccion;

    public AnomaliaRequestDTO() {}

    public AnomaliaRequestDTO(Integer idSiembra, String nombre, TipoAnomalia tipo, EstadoAnomalia estado,
                             String descripcion, NivelSeveridad nivelSeveridad, LocalDateTime fechaDeteccion) {
        this.idSiembra = idSiembra;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.nivelSeveridad = nivelSeveridad;
        this.fechaDeteccion = fechaDeteccion;
    }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

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

    @Override
    public String toString() {
        return "AnomaliaRequestDTO{" +
                "idSiembra=" + idSiembra +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nivelSeveridad='" + nivelSeveridad + '\'' +
                ", fechaDeteccion=" + fechaDeteccion +
                '}';
    }
}
