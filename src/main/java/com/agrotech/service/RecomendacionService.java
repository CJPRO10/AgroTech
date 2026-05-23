package com.agrotech.service;

import com.agrotech.Entity.enums.EstadoRecomendacion;
import com.agrotech.Entity.enums.PrioridadRecomendacion;
import com.agrotech.dto.request.RecomendacionRequestDTO;
import com.agrotech.dto.response.RecomendacionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface RecomendacionService {

    RecomendacionResponseDTO solicitar(RecomendacionRequestDTO dto);
    List<RecomendacionResponseDTO> listar(String correo);
    List<RecomendacionResponseDTO> listarPorSiembra(Integer idSiembra);
    List<RecomendacionResponseDTO> listarPorAnomalia(Integer idAnomalia);
    List<RecomendacionResponseDTO> listarPorCategoria(String categoria);
    List<RecomendacionResponseDTO> listarPorPrioridad(PrioridadRecomendacion prioridad);
    List<RecomendacionResponseDTO> listarPorEstado(EstadoRecomendacion estado);
    List<RecomendacionResponseDTO> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta);
    RecomendacionResponseDTO buscarPorId(Integer idRecomendacion);
    RecomendacionResponseDTO ignorar(Integer idRecomendacion);
    RecomendacionResponseDTO reaccionar(Integer idRecomendacion, String reaccion);
    void generarRecomendacionesClimaticas();
}