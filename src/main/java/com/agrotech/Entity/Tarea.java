package com.agrotech.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Integer idTarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_tarea", nullable = false)
    private TipoTarea tipoTarea;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "fecha_limite", nullable = false)
    private LocalDateTime fechaLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_siembra", nullable = false)
    private Siembra siembra;

    @OneToMany(mappedBy = "tarea", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<EjecucionTarea> ejecuciones;

    public Tarea() {}

    public Integer getIdTarea() { return idTarea; }
    public void setIdTarea(Integer idTarea) { this.idTarea = idTarea; }

    public TipoTarea getTipoTarea() { return tipoTarea; }
    public void setTipoTarea(TipoTarea tipoTarea) { this.tipoTarea = tipoTarea; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public Siembra getSiembra() { return siembra; }
    public void setSiembra(Siembra siembra) { this.siembra = siembra; }

    public List<EjecucionTarea> getEjecuciones() { return ejecuciones; }
    public void setEjecuciones(List<EjecucionTarea> ejecuciones) { this.ejecuciones = ejecuciones; }
}