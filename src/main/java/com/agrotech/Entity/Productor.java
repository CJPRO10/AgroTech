package com.agrotech.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "productores")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class Productor extends Usuario{
    @OneToMany(mappedBy = "productor", fetch = FetchType.LAZY)
    private List<Finca> fincas;

    @OneToMany(mappedBy = "usuarioProductor", fetch = FetchType.LAZY)
    private List<Finanza> finanzas;

    @OneToMany(mappedBy = "usuarioProductor", fetch = FetchType.LAZY)
    private List<Reporte> reportes;

    @OneToMany(mappedBy = "productor", fetch = FetchType.LAZY)
    private List<Operario> operarios;

    @OneToMany(mappedBy = "productor", fetch = FetchType.LAZY)
    private List<Auxiliar> auxiliares;

    public Productor() {}

    public List<Finca> getFincas() { return fincas; }
    public void setFincas(List<Finca> fincas) { this.fincas = fincas; }

    public List<Finanza> getFinanzas() { return finanzas; }
    public void setFinanzas(List<Finanza> finanzas) { this.finanzas = finanzas; }

    public List<Reporte> getReportes() { return reportes; }
    public void setReportes(List<Reporte> reportes) { this.reportes = reportes; }

    public List<Operario> getOperarios() { return operarios; }
    public void setOperarios(List<Operario> operarios) { this.operarios = operarios; }

    public List<Auxiliar> getAuxiliares() { return auxiliares; }
    public void setAuxiliares(List<Auxiliar> auxiliares) { this.auxiliares = auxiliares; }

    @Override
    public String toString() {
        return "Productor{idUsuario=" + getIdUsuario() + ", nombre='" + getNombre() + "'}";
    }

}
