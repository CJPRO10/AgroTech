package com.agrotech.service;

import com.agrotech.dto.request.ReporteRequestDTO;
import com.agrotech.dto.response.ReporteResponseDTO;
import java.util.List;

public interface ReporteService {
    ReporteResponseDTO crear(String correo, ReporteRequestDTO dto);
    List<ReporteResponseDTO> listar(String correo);
    List<ReporteResponseDTO> buscarPorNombre(String correo, String nombre);
    List<ReporteResponseDTO> filtrarPorFormato(String correo, String formato);
    List<ReporteResponseDTO> filtrarPorPeriodicidad(String correo, String periodicidad);
    void eliminar(String correo, Integer idReporte);
}
