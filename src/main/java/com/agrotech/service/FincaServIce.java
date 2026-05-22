package com.agrotech.service;

import com.agrotech.dto.request.FincaRequestDTO;
import com.agrotech.dto.request.FincaUpdateRequestDTO;
import com.agrotech.dto.response.FincaResponseDTO;

import java.util.List;

public interface FincaServIce {

    List<FincaResponseDTO> listarPorCorreo(String correo);
    List<FincaResponseDTO> buscarPorNombre(String nombre, String correo);
    FincaResponseDTO crear(String correo, FincaRequestDTO dto);
    FincaResponseDTO actualizar(Integer idFinca, String correo, FincaUpdateRequestDTO dto);
    void eliminar(Integer idFinca, String correo);
}
