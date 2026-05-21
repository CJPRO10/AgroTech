package com.agrotech.dto.request;

import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;

public class UsuarioUpdateRequestDTO {

    private String nombre;
    private String apellido;

    @Email(message = "Formato de correo inválido")
    private String correo;

    private String contrasena;
    private String telefono;
    private LocalDateTime fechaNacimiento;
    private String rol;

    public UsuarioUpdateRequestDTO() {}

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

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}