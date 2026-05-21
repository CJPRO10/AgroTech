package com.agrotech.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class RegistroProductorRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDateTime fechaNacimiento;

    // Datos de la finca (obligatorios)
    @NotBlank(message = "El nombre de la finca es obligatorio")
    private String nombreFinca;

    @NotNull(message = "Las hectáreas son obligatorias")
    @DecimalMax(value = "5.0", message = "Las hectáreas no pueden exceder 5")
    @DecimalMin(value = "0.01", message = "Las hectáreas deben ser mayor a 0")
    private Double hectareas;

    @NotNull(message = "El número de lotes es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 lote")
    private Integer numLotes;

    @NotNull(message = "La ubicación es obligatoria")
    private Integer idUbicacion;

    public RegistroProductorRequestDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public LocalDateTime getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDateTime fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getNombreFinca() { return nombreFinca; }
    public void setNombreFinca(String nombreFinca) { this.nombreFinca = nombreFinca; }

    public Double getHectareas() { return hectareas; }
    public void setHectareas(Double hectareas) { this.hectareas = hectareas; }

    public Integer getNumLotes() { return numLotes; }
    public void setNumLotes(Integer numLotes) { this.numLotes = numLotes; }

    public Integer getIdUbicacion() { return idUbicacion; }
    public void setIdUbicacion(Integer idUbicacion) { this.idUbicacion = idUbicacion; }
}
