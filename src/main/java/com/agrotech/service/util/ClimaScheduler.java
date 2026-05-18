package com.agrotech.service.util;

import com.agrotech.repository.UbicacionRepository;
import com.agrotech.service.ClimaService;
import com.agrotech.service.RecomendacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClimaScheduler {

    private final ClimaService climaService;
    private final UbicacionRepository ubicacionRepository;
    private final RecomendacionService recomendacionService;

    public ClimaScheduler(ClimaService climaService, UbicacionRepository ubicacionRepository, RecomendacionService recomendacionService) {
        this.climaService = climaService;
        this.ubicacionRepository = ubicacionRepository;
        this.recomendacionService = recomendacionService;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void actualizarClimaTodasLasUbicaciones() {
        ubicacionRepository.findAll().forEach(ubicacion -> {
            try {
                climaService.consultarClimaActual(ubicacion.getIdUbicacion());
            } catch (Exception ex) {
                System.err.println("Error actualizando clima para ubicacion " + ubicacion.getIdUbicacion() + ": " + ex.getMessage());
            }
        });

        try {
            recomendacionService.generarRecomendacionesClimaticas();
        } catch (Exception ex) {
            System.err.println("Error generando recomendacionesClimaticas: " + ex.getMessage());
        }
    }
}
