package com.agrotech.dto.response;

public class LoginResponseDTO {

    private String token;
    private String correo;
    private String nombreCompleto;
    private String rol;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, String correo, String nombreCompleto, String rol) {
        this.token = token;
        this.correo = correo;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombre) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
}