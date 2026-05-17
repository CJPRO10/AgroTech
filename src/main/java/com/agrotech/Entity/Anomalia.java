package com.agrotech.Entity;
import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anomalias")
public class Anomalia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_anomalia")
    private Integer idAnomalia;

    @Column(name = "nombre", length = 300)
    private String nombre;

    @Column(name = "tipo", length = 250)
    @Enumerated(EnumType.STRING)
    private TipoAnomalia tipo;

    @Column(name = "estado", length = 250)
    @Enumerated(EnumType.STRING)
    private EstadoAnomalia estado;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "nivel_severidad", length = 100)
    @Enumerated(EnumType.STRING)
    private NivelSeveridad nivelSeveridad;

    @Column(name = "fecha_deteccion")
    private LocalDateTime fechaDeteccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_siembra")
    private Siembra siembra;
/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
*/
    public Anomalia() {}

    public Anomalia(Integer idAnomalia, String nombre, TipoAnomalia tipo, EstadoAnomalia estado,
                    String descripcion, NivelSeveridad nivelSeveridad, LocalDateTime fechaDeteccion,
                    Siembra siembra) {
        this.idAnomalia = idAnomalia;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.descripcion = descripcion;
        this.nivelSeveridad = nivelSeveridad;
        this.fechaDeteccion = fechaDeteccion;
        this.siembra = siembra;
        //this.usuario = usuario;
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

    public Siembra getSiembra() { return siembra; }
    public void setSiembra(Siembra siembra) { this.siembra = siembra; }
/*
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
*/
    @Override
    public String toString() {
        return "Anomalia{" +
                "idAnomalia=" + idAnomalia +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estado='" + estado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", nivelSeveridad='" + nivelSeveridad + '\'' +
                ", fechaDeteccion=" + fechaDeteccion +
                '}';
    }
}
