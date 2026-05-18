package com.agrotech.Entity;

import java.time.LocalDateTime;
import java.util.List;
import com.agrotech.Entity.enums.EstadoRecomendacion;
import com.agrotech.Entity.enums.PrioridadRecomendacion;
import jakarta.persistence.*;

@Entity
@Table(name = "recomendaciones")
public class Recomendacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recomendacion")
    private Integer idRecomendacion;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "prioridad", length = 50)
    @Enumerated(EnumType.STRING)
    private PrioridadRecomendacion prioridad;

    @Column(name = "estado", length = 50)
    @Enumerated(EnumType.STRING)
    private EstadoRecomendacion estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clima")
    private Clima clima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_anomalia")
    private Anomalia anomalia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_siembra")
    private Siembra siembra;

    @OneToMany(mappedBy = "recomendacion", fetch = FetchType.LAZY)
    private List<Reaccion> reacciones;

    public Recomendacion() {}

    public Recomendacion(Integer idRecomendacion, String descripcion,
                         LocalDateTime fechaGeneracion, String categoria, PrioridadRecomendacion prioridad,
                         EstadoRecomendacion estado, Anomalia anomalia, Siembra siembra, Clima clima) {
        this.idRecomendacion = idRecomendacion;
        this.descripcion = descripcion;
        this.fechaGeneracion = fechaGeneracion;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.estado = estado;
        this.anomalia = anomalia;
        this.siembra = siembra;
        this.clima = clima;
    }

    public Integer getIdRecomendacion() { return idRecomendacion; }
    public void setIdRecomendacion(Integer idRecomendacion) { this.idRecomendacion = idRecomendacion; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public Clima getClima() { return clima; }
    public void setClima(Clima clima) { this.clima = clima; }

    public List<Reaccion> getReacciones() { return reacciones; }
    public void setReacciones(List<Reaccion> reacciones) { this.reacciones = reacciones; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public PrioridadRecomendacion getPrioridad() { return prioridad; }
    public void setPrioridad(PrioridadRecomendacion prioridad) { this.prioridad = prioridad; }

    public EstadoRecomendacion getEstado() { return estado; }
    public void setEstado(EstadoRecomendacion estado) { this.estado = estado; }

    public Anomalia getAnomalia() { return anomalia; }
    public void setAnomalia(Anomalia anomalia) { this.anomalia = anomalia; }

    public Siembra getSiembra() { return siembra;}
    public void setSiembra(Siembra siembra) { this.siembra = siembra; }

    @Override
    public String toString() {
        return "Recomendacion{" +
                "idRecomendacion=" + idRecomendacion +
                ", descripcion='" + descripcion + '\'' +
                ", fechaGeneracion=" + fechaGeneracion +
                ", categoria='" + categoria + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                ", anomalia=" + anomalia +
                ", siembra=" + siembra +
                ", reacciones=" + reacciones +
                '}';
    }
}
