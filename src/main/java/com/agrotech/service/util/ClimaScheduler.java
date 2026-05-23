package com.agrotech.service.util;

import com.agrotech.repository.ClimaRepository;
import com.agrotech.repository.UbicacionRepository;
import com.agrotech.service.ClimaService;
import com.agrotech.service.NotificacionService;
import com.agrotech.service.RecomendacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClimaScheduler {

    private final ClimaService climaService;
    private final UbicacionRepository ubicacionRepository;
    private final RecomendacionService recomendacionService;
    private final NotificacionService notificacionService;
    private final ClimaRepository climaRepository;

    public ClimaScheduler(ClimaService climaService,
                          UbicacionRepository ubicacionRepository,
                          RecomendacionService recomendacionService,
                          NotificacionService notificacionService,
                          ClimaRepository climaRepository) {
        this.climaService = climaService;
        this.ubicacionRepository = ubicacionRepository;
        this.recomendacionService = recomendacionService;
        this.notificacionService = notificacionService;
        this.climaRepository = climaRepository;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void actualizarClimaTodasLasUbicaciones() {
        ubicacionRepository.findAll().forEach(ubicacion -> {
            try {
                climaService.consultarClimaActual(ubicacion.getIdUbicacion());

                climaRepository
                        .findTopByUbicacion_IdUbicacionAndFechaMedicionOrderByFechaRegistroDesc(
                                ubicacion.getIdUbicacion(), java.time.LocalDate.now())
                        .ifPresent(clima -> {
                            String alerta = climaService.evaluarAlerta(
                                    clima.getTemperatura().doubleValue(),
                                    clima.getPrecipitacion(),
                                    clima.getCondicion()
                            );
                            if (alerta != null) {
                                notificacionService.generarNotificacionClima(clima, alerta);
                            }
                        });

            } catch (Exception ex) {
                System.err.println("Error actualizando clima para ubicacion "
                        + ubicacion.getIdUbicacion() + ": " + ex.getMessage());
            }
        });

        try {
            recomendacionService.generarRecomendacionesClimaticas();
        } catch (Exception ex) {
            System.err.println("Error generando recomendaciones climáticas: " + ex.getMessage());
        }
    }
}