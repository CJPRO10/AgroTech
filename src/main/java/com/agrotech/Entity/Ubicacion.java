package com.agrotech.Entity;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "ubicaciones")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Integer idUbicacion;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "latitud", length = 100)
    private double latitud;

    @Column(name = "longitud", length = 100)
    private double longitud;

    @OneToMany(mappedBy = "ubicacion", fetch = FetchType.LAZY)
    private List<Clima> climas;

    @OneToMany(mappedBy = "ubicacion", fetch = FetchType.LAZY)
    private List<Finca> fincas;

    public Ubicacion() {}

    public Ubicacion(Integer idUbicacion, String nombre, double latitud, double longitud) {
        this.idUbicacion = idUbicacion;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public List<Clima> getClimas() { return climas; }
    public void setClimas(List<Clima> climas) { this.climas = climas; }

    public List<Finca> getFincas() { return fincas; }
    public void setFincas(List<Finca> fincas) { this.fincas = fincas; }

    @Override
    public String toString() {
        return "Ubicacion{idUbicacion=" + idUbicacion + ", nombre='" + nombre +
                "', latitud='" + latitud + "', longitud='" + longitud + "'}";
    }

}
