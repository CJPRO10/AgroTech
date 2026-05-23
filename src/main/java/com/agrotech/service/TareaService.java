package com.agrotech.service;

import com.agrotech.dto.request.ActualizarEstadoTareaRequestDTO;
import com.agrotech.dto.request.AsignarTareaRequestDTO;
import com.agrotech.dto.request.TareaRequestDTO;
import com.agrotech.dto.response.EjecucionTareaResponseDTO;
import com.agrotech.dto.response.TareaResponseDTO;

import java.util.List;

public interface TareaService {
    TareaResponseDTO crear(TareaRequestDTO dto, String correoCreador);
    TareaResponseDTO asignar(Integer idTarea, AsignarTareaRequestDTO dto, String correoAsignador);
    List<TareaResponseDTO> listar(String correoSolicitante);
    TareaResponseDTO buscarPorId(Integer idTarea, String correoSolicitante);
    List<TareaResponseDTO> listarPorSiembra(Integer idSiembra, String correoSolicitante);
    EjecucionTareaResponseDTO actualizarEstado(Integer idEjecucion,
                                               ActualizarEstadoTareaRequestDTO dto,
                                               String correoSolicitante);
}
