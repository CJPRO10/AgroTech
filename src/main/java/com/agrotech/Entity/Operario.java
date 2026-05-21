package com.agrotech.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "operario")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Operario extends Usuario{
    @OneToMany(mappedBy = "operario", fetch = FetchType.LAZY)
    private List<EjecucionTarea> ejecucionesTareas;

    @OneToMany(mappedBy = "operario", fetch = FetchType.LAZY)
    private List<Reaccion> reacciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_productor")
    private Productor productor;

    public Operario() {}

    public List<EjecucionTarea> getEjecucionesTareas() { return ejecucionesTareas; }
    public void setEjecucionesTareas(List<EjecucionTarea> ejecucionesTareas) {
        this.ejecucionesTareas = ejecucionesTareas;
    }

    public List<Reaccion> getReacciones() { return reacciones; }
    public void setReacciones(List<Reaccion> reacciones) { this.reacciones = reacciones; }

    public Productor getProductor() { return productor; }
    public void setProductor(Productor productor) { this.productor = productor; }

    @Override
    public String toString() {
        return "Operario{idUsuario=" + getIdUsuario() + ", nombre='" + getNombre() + "'}";
    }

}
