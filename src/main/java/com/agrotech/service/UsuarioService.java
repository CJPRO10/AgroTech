package com.agrotech.service;

import com.agrotech.dto.request.EditarPerfilRequestDTO;
import com.agrotech.dto.request.UsuarioRequestDTO;
import com.agrotech.dto.request.UsuarioUpdateRequestDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioResponseDTO crear(UsuarioRequestDTO dto, String correoCreador);

    List<UsuarioResponseDTO> listar(String correoSolicitante);

    List<UsuarioResponseDTO> buscarPorNombre(String nombre, String correoSolicitante);

    List<UsuarioResponseDTO> buscarPorCorreo(String correo, String correoSolicitante);

    List<UsuarioResponseDTO> buscarPorRol(String nombreRol, String correoSolicitante);

    UsuarioResponseDTO actualizar(Integer idUsuario, UsuarioUpdateRequestDTO dto);

    void eliminar(Integer idUsuario);

    UsuarioResponseDTO desactivar(Integer idUsuario, String correoSolicitante);

    UsuarioResponseDTO verPerfil(String correo);

    UsuarioResponseDTO editarPerfil(String correo, EditarPerfilRequestDTO dto);

    void eliminarPerfil(String correo);
}