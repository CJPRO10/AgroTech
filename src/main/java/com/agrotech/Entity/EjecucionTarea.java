package com.agrotech.Entity;

import com.agrotech.Entity.enums.EstadoTarea;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ejecuciones_tareas")
public class EjecucionTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejecucion_tarea")
    private Integer idEjecucionTarea;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTarea estado;

    @Column(name = "fecha_estado")
    private LocalDateTime fechaEstado;

    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tarea", nullable = false)
    private Tarea tarea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_creado_por", nullable = false)
    private Usuario creadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_operativo")
    private Operario operario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auxiliar")
    private Auxiliar auxiliar;

    public EjecucionTarea() {}

    public Integer getIdEjecucionTarea() { return idEjecucionTarea; }
    public void setIdEjecucionTarea(Integer idEjecucionTarea) { this.idEjecucionTarea = idEjecucionTarea; }

    public EstadoTarea getEstado() { return estado; }
    public void setEstado(EstadoTarea estado) { this.estado = estado; }

    public LocalDateTime getFechaEstado() { return fechaEstado; }
    public void setFechaEstado(LocalDateTime fechaEstado) { this.fechaEstado = fechaEstado; }

    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }

    public Tarea getTarea() { return tarea; }
    public void setTarea(Tarea tarea) { this.tarea = tarea; }

    public Usuario getCreadoPor() { return creadoPor; }
    public void setCreadoPor(Usuario creadoPor) { this.creadoPor = creadoPor; }

    public Operario getOperario() { return operario; }
    public void setOperario(Operario operario) { this.operario = operario; }

    public Auxiliar getAuxiliar() { return auxiliar; }
    public void setAuxiliar(Auxiliar auxiliar) { this.auxiliar = auxiliar; }

    @Override
    public String toString() {
        return "EjecucionTarea{idEjecucionTarea=" + idEjecucionTarea +
                ", estado=" + estado +
                ", fechaEstado=" + fechaEstado + "}";
    }
}