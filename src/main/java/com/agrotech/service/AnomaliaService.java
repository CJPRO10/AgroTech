package com.agrotech.service;

import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;
import com.agrotech.dto.request.AnomaliaRequestDTO;
import com.agrotech.dto.response.AnomaliaResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AnomaliaService {

    // Registrar anomalía - Generar recomendación automáticamente
    AnomaliaResponseDTO registrar(AnomaliaRequestDTO dto);

    // Consultar todas
    List<AnomaliaResponseDTO> listar();

    // Consultar por siembra
    List<AnomaliaResponseDTO> listarPorSiembra(Integer idSiembra);

    // Filtrar por tipo
    List<AnomaliaResponseDTO> listarPorTipo(TipoAnomalia tipo);

    // Filtrar por estado
    List<AnomaliaResponseDTO> listarPorEstado(EstadoAnomalia estado);

    // Filtrar por nivel de severidad
    List<AnomaliaResponseDTO> listarPorNivelSeveridad(NivelSeveridad nivelSeveridad);

    // Filtrar por rango de fechas
    List<AnomaliaResponseDTO> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta);

    // Buscar por id
    AnomaliaResponseDTO buscarPorId(Integer idAnomalia);

    // Actualizar
    AnomaliaResponseDTO actualizar(Integer idAnomalia, AnomaliaRequestDTO dto);

    // Eliminar
    void eliminar(Integer idAnomalia);
}