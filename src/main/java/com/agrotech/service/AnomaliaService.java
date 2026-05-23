package com.agrotech.service;

import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;
import com.agrotech.dto.request.AnomaliaRequestDTO;
import com.agrotech.dto.response.AnomaliaResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AnomaliaService {

    AnomaliaResponseDTO registrar(AnomaliaRequestDTO dto, String correo);
    List<AnomaliaResponseDTO> listar(String correo);
    List<AnomaliaResponseDTO> listarPorSiembra(Integer idSiembra, String correo);
    List<AnomaliaResponseDTO> listarPorTipo(TipoAnomalia tipo, String correo);
    List<AnomaliaResponseDTO> listarPorEstado(EstadoAnomalia estado, String correo);
    List<AnomaliaResponseDTO> listarPorNivelSeveridad(NivelSeveridad nivelSeveridad, String correo);
    List<AnomaliaResponseDTO> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta, String correo);
    AnomaliaResponseDTO buscarPorId(Integer idAnomalia, String correo);
    AnomaliaResponseDTO actualizar(Integer idAnomalia, AnomaliaRequestDTO dto, String correo);
    void eliminar(Integer idAnomalia, String correo);
}