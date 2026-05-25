package com.agrotech.dto.request;

public class FinanzaRequestDTO {
    private String descripcion;
    private Float monto;
    private String tipoTransaccion;
    private String categoria;
    private String fechaRegistro; // ISO string desde el frontend

    public FinanzaRequestDTO() {}

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Float getMonto() { return monto; }
    public void setMonto(Float monto) { this.monto = monto; }

    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
