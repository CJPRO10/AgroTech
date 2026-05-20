package com.agrotech.service;

import com.agrotech.dto.request.LoginRequestDTO;
import com.agrotech.dto.request.RegistroProductorRequestDTO;
import com.agrotech.dto.response.LoginResponseDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    UsuarioResponseDTO registrarProductor(RegistroProductorRequestDTO registroProductorRequestDTO);
}