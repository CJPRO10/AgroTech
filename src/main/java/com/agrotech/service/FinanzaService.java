package com.agrotech.service;

import com.agrotech.dto.request.FinanzaRequestDTO;
import com.agrotech.dto.response.FinanzaResponseDTO;
import java.util.List;

public interface FinanzaService {
    FinanzaResponseDTO crear(String correo, FinanzaRequestDTO dto);
    List<FinanzaResponseDTO> listar(String correo);
    FinanzaResponseDTO actualizar(String correo, Integer idFinanza, FinanzaRequestDTO dto);
    void eliminar(String correo, Integer idFinanza);
    List<FinanzaResponseDTO> filtrarPorTipo(String correo, String tipo);
    List<FinanzaResponseDTO> filtrarPorCategoria(String correo, String categoria);
}
