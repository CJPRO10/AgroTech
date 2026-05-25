package com.agrotech.service;

import com.agrotech.Entity.Ubicacion;
import com.agrotech.dto.request.UbicacionRequestDTO;

import java.util.List;

public interface UbicacionService {
    List<Ubicacion> listar();
    List<Ubicacion> buscarPorNombre(String nombre);
    Ubicacion crearOReusar(UbicacionRequestDTO dto);
}