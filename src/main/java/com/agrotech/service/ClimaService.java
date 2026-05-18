package com.agrotech.service;

import com.agrotech.dto.response.ClimaResponseDTO;
import com.agrotech.dto.response.PronosticoResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ClimaService {

    // Consultar clima actual (llama a Open-Meteo y guarda en BD)
    ClimaResponseDTO consultarClimaActual(Integer idUbicacion);

    // Consultar pronostico de los próximos N días
    PronosticoResponseDTO consultarPronostico(Integer idUbicacion, int dias);

    // Consultar historial climatico guardado en BD
    List<ClimaResponseDTO> consultarHistorial(Integer idUbicacion);

    // Historial filtrado por rango de fechas
    List<ClimaResponseDTO> consultarHistorialPorRango(Integer idUbicacion,
                                                      LocalDate desde,
                                                      LocalDate hasta);

    //  Evaluar condicion climatica (sin implementar aun)
    String evaluarAlerta(double temperatura, float precipitacion, String condicion);
}