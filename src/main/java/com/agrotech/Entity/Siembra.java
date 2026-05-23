package com.agrotech.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "siembras")
public class Siembra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_siembra")
    private Integer idSiembra;

    @Column(name = "num_lote")
    private Integer numLote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cultivo", nullable = false)
    private Cultivo cultivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_finca", nullable = false)
    private Finca finca;

    @OneToMany(mappedBy = "siembra", fetch = FetchType.LAZY)
    private List<SiembraEstadoCultivo> estadosCultivo;

    @OneToMany(mappedBy = "siembra", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tarea> tareas;

    public Siembra() {}

    public Siembra(Integer idSiembra, Cultivo cultivo, Finca finca, Integer numLote) {
         this.numLote = numLote;
        this.idSiembra = idSiembra;
        this.cultivo = cultivo;
        this.finca = finca;
        this.numLote = numLote;
    }

    public Integer getIdSiembra() { return idSiembra; }
    public void setIdSiembra(Integer idSiembra) { this.idSiembra = idSiembra; }

    public Cultivo getCultivo() { return cultivo; }
    public void setCultivo(Cultivo cultivo) { this.cultivo = cultivo; }

    public Finca getFinca() { return finca; }
    public void setFinca(Finca finca) { this.finca = finca; }

    public List<SiembraEstadoCultivo> getEstadosCultivo() { return estadosCultivo; }
    public void setEstadosCultivo(List<SiembraEstadoCultivo> estadosCultivo) {
        this.estadosCultivo = estadosCultivo;
    }

    public Integer getNumLote() { return numLote; }
    public void setNumLote(Integer numLote) { this.numLote = numLote; }

    public List<Tarea> getTareas() { return tareas; }
    public void setTareas(List<Tarea> tareas) { this.tareas = tareas; }

    @Override
    public String toString() {
        return "Siembra{" +
                "idSiembra=" + idSiembra +
                ", numLote=" + numLote +
                ", cultivo=" + cultivo +
                ", finca=" + finca +
                ", estadosCultivo=" + estadosCultivo +
                '}';
    }
}
