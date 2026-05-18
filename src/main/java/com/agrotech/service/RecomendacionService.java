package com.agrotech.service;

import com.agrotech.Entity.enums.EstadoRecomendacion;
import com.agrotech.Entity.enums.PrioridadRecomendacion;
import com.agrotech.dto.request.RecomendacionRequestDTO;
import com.agrotech.dto.response.RecomendacionResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface RecomendacionService {

   // Solicitar recomendación
    RecomendacionResponseDTO solicitar(RecomendacionRequestDTO dto);

    // Consultar todas
    List<RecomendacionResponseDTO> listar();

    // Consultar por siembra
    List<RecomendacionResponseDTO> listarPorSiembra(Integer idSiembra);

    // Consultar por anomalía
    List<RecomendacionResponseDTO> listarPorAnomalia(Integer idAnomalia);

    // Filtrar por categoría
    List<RecomendacionResponseDTO> listarPorCategoria(String categoria);

    // Filtrar por prioridad
    List<RecomendacionResponseDTO> listarPorPrioridad(PrioridadRecomendacion prioridad);

    // Filtrar por estado
    List<RecomendacionResponseDTO> listarPorEstado(EstadoRecomendacion estado);

    // Filtrar por rango de fechas
    List<RecomendacionResponseDTO> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta);

    // Buscar por id
    RecomendacionResponseDTO buscarPorId(Integer idRecomendacion);

    // Reaccionar a la recomendacion
    RecomendacionResponseDTO ignorar(Integer idRecomendacion);
    RecomendacionResponseDTO reaccionar(Integer idRecomendacion, String reaccion);

    // Generar recomendaciones climaticas automaticas
    void generarRecomendacionesClimaticas();
}