package com.agrotech.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tipos_tarea")
public class TipoTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_tarea")
    private Integer idTipoTarea;

    @Column(name = "nombre", length = 250, nullable = false, unique = true)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoTarea", fetch = FetchType.LAZY)
    private List<Tarea> tareas;

    public TipoTarea() {}

    public Integer getIdTipoTarea() { return idTipoTarea; }
    public void setIdTipoTarea(Integer idTipoTarea) { this.idTipoTarea = idTipoTarea; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Tarea> getTareas() { return tareas; }
    public void setTareas(List<Tarea> tareas) { this.tareas = tareas; }
}