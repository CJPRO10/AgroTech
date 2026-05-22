package com.agrotech.service;

import com.agrotech.dto.request.CultivoRequestDTO;
import com.agrotech.dto.request.CultivoUpdateRequestDTO;
import com.agrotech.dto.response.CultivoResponseDTO;

import java.util.List;

public interface CultivoService {

    CultivoResponseDTO crear(CultivoRequestDTO cultivoRequestDTO);
    List<CultivoResponseDTO> buscarPorNombre(String nombre);
    List<CultivoResponseDTO>buscarPorTipo(Integer idTipoCultivo);
    List<CultivoResponseDTO> listarTodos();
    CultivoResponseDTO actualizar(Integer id, CultivoUpdateRequestDTO dto);
    void eliminar(Integer id);
}
