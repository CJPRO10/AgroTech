package com.agrotech.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "auxiliares")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Auxiliar extends Usuario{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_productor")
    private Productor productor;

    @OneToMany(mappedBy = "auxiliar", fetch = FetchType.LAZY)
    private List<EjecucionTarea> ejecucionesTareas;

    public Auxiliar() {}

    public List<EjecucionTarea> getEjecucionesTareas() { return ejecucionesTareas; }
    public void setEjecucionesTareas(List<EjecucionTarea> ejecucionesTareas) {
        this.ejecucionesTareas = ejecucionesTareas;
    }

    public Productor getProductor() { return productor; }
    public void setProductor(Productor productor) { this.productor = productor; }

    @Override
    public String toString() {
        return "Auxiliar{idUsuario=" + getIdUsuario() + ", nombre='" + getNombre() + "'}";
    }
}
