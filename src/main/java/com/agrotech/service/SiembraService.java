package com.agrotech.service;

import com.agrotech.dto.request.SiembraRequestDTO;
import com.agrotech.dto.request.SiembraUpdateRequestDTO;
import com.agrotech.dto.response.SiembraResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SiembraService {

    SiembraResponseDTO crear(SiembraRequestDTO dto, String correo);
    List<SiembraResponseDTO> listar(String correo);
    List<SiembraResponseDTO> buscarPorFinca(Integer idFinca, String correo);
    List<SiembraResponseDTO> buscarPorCultivo(Integer idCultivo, String correo);
    List<SiembraResponseDTO> buscarPorFincaYCultivo(Integer idFinca, Integer idCultivo, String correo);
    List<SiembraResponseDTO> buscarPorFincaYLote(Integer idFinca, Integer numLote, String correo);
    List<SiembraResponseDTO> buscarPorEstado(Integer idEstado, String correo);
    List<SiembraResponseDTO> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta, String correo);
    SiembraResponseDTO actualizar(Integer idSiembra, SiembraUpdateRequestDTO dto, String correo);
    void eliminar(Integer idSiembra, String correo);
}
