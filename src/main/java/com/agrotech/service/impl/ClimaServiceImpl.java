package com.agrotech.service.impl;

import com.agrotech.Entity.Clima;
import com.agrotech.Entity.Ubicacion;
import com.agrotech.dto.response.ClimaResponseDTO;
import com.agrotech.dto.response.PronosticoResponseDTO;
import com.agrotech.mapper.ClimaMapper;
import com.agrotech.repository.ClimaRepository;
import com.agrotech.repository.UbicacionRepository;
import com.agrotech.service.ClimaService;
import com.agrotech.service.NotificacionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ClimaServiceImpl implements ClimaService {

    private static final String OPEN_METEO_ACTUAL =
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude={lat}&longitude={lon}" +
                    "&current=temperature_2m,precipitation,weathercode" +
                    "&timezone=auto";

    private static final String OPEN_METEO_PRONOSTICO =
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude={lat}&longitude={lon}" +
                    "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum,weathercode" +
                    "&forecast_days={dias}" +
                    "&timezone=auto";

    private final ClimaRepository climaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final ClimaMapper climaMapper;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final NotificacionService notificacionService;

    public ClimaServiceImpl(ClimaRepository climaRepository,
                            UbicacionRepository ubicacionRepository,
                            ClimaMapper climaMapper,
                            RestTemplate restTemplate,
                            ObjectMapper objectMapper,
                            NotificacionService notificacionService) {
        this.climaRepository = climaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.climaMapper = climaMapper;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.notificacionService = notificacionService;
    }

    @Override
    public ClimaResponseDTO consultarClimaActual(Integer idUbicacion) {
        Ubicacion ubicacion = obtenerUbicacion(idUbicacion);

        double lat = ubicacion.getLatitud();
        double lon = ubicacion.getLongitud();

        try {
            String url = OPEN_METEO_ACTUAL
                    .replace("{lat}", String.valueOf(lat))
                    .replace("{lon}", String.valueOf(lon));

            String json = restTemplate.getForObject(url, String.class);
            JsonNode root    = objectMapper.readTree(json);
            JsonNode current = root.path("current");

            double tempRaw     = current.path("temperature_2m").asDouble();
            float  precip      = (float) current.path("precipitation").asDouble();
            int    weatherCode = current.path("weathercode").asInt();
            String condicion   = interpretarCodigo(weatherCode);

            BigDecimal temperatura = BigDecimal.valueOf(tempRaw)
                    .setScale(2, RoundingMode.HALF_UP);

            LocalDate hoy = LocalDate.now();
            Clima clima = climaRepository
                    .findTopByUbicacion_IdUbicacionAndFechaMedicionOrderByFechaRegistroDesc(
                            idUbicacion, hoy)
                    .orElseGet(() -> {
                        Clima nuevo = new Clima();
                        nuevo.setUbicacion(ubicacion);
                        nuevo.setFechaMedicion(hoy);
                        nuevo.setFechaRegistro(hoy);
                        return nuevo;
                    });

            clima.setTemperatura(temperatura);
            clima.setPrecipitacion(precip);
            clima.setCondicion(condicion);
            clima = climaRepository.save(clima);

            String alerta = evaluarAlerta(temperatura.doubleValue(), precip, condicion);
            if (alerta != null) {
                notificacionService.generarNotificacionClima(clima, alerta);
            }

            return climaMapper.toResponse(clima);
        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el clima desde Open-Meteo: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PronosticoResponseDTO consultarPronostico(Integer idUbicacion, int dias) {
        if (dias < 1 || dias > 16) {
            throw new RuntimeException("El número de días debe estar entre 1 y 16.");
        }

        Ubicacion ubicacion = obtenerUbicacion(idUbicacion);
        double lat = ubicacion.getLatitud();
        double lon = ubicacion.getLongitud();

        try {
            String url = OPEN_METEO_PRONOSTICO
                    .replace("{lat}", String.valueOf(lat))
                    .replace("{lon}", String.valueOf(lon))
                    .replace("{dias}", String.valueOf(dias));

            String json  = restTemplate.getForObject(url, String.class);
            JsonNode root  = objectMapper.readTree(json);
            JsonNode daily = root.path("daily");

            JsonNode fechas    = daily.path("time");
            JsonNode tempMax   = daily.path("temperature_2m_max");
            JsonNode tempMin   = daily.path("temperature_2m_min");
            JsonNode precipArr = daily.path("precipitation_sum");
            JsonNode wCodes    = daily.path("weathercode");

            List<PronosticoResponseDTO.DiasPronostico> pronosticos = new ArrayList<>();

            for (int i = 0; i < fechas.size(); i++) {
                LocalDate  fecha     = LocalDate.parse(fechas.get(i).asText());
                BigDecimal tMax      = BigDecimal.valueOf(tempMax.get(i).asDouble())
                        .setScale(1, RoundingMode.HALF_UP);
                BigDecimal tMin      = BigDecimal.valueOf(tempMin.get(i).asDouble())
                        .setScale(1, RoundingMode.HALF_UP);
                float      precip    = (float) precipArr.get(i).asDouble();
                String     condicion = interpretarCodigo(wCodes.get(i).asInt());
                String     alerta    = evaluarAlerta(tMax.doubleValue(), precip, condicion);

                pronosticos.add(new PronosticoResponseDTO.DiasPronostico(
                        fecha, tMax, tMin, precip, condicion, null));
            }

            return new PronosticoResponseDTO(ubicacion.getNombre(), idUbicacion, pronosticos);

        } catch (Exception e) {
            throw new RuntimeException("Error al consultar el pronóstico desde Open-Meteo: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClimaResponseDTO> consultarHistorial(Integer idUbicacion) {
        obtenerUbicacion(idUbicacion);
        return climaRepository
                .findByUbicacion_IdUbicacionOrderByFechaMedicionDesc(idUbicacion)
                .stream()
                .map(climaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClimaResponseDTO> consultarHistorialPorRango(Integer idUbicacion,
                                                             LocalDate desde,
                                                             LocalDate hasta) {
        obtenerUbicacion(idUbicacion);
        return climaRepository
                .findByUbicacion_IdUbicacionAndFechaMedicionBetweenOrderByFechaMedicionDesc(
                        idUbicacion, desde, hasta)
                .stream()
                .map(climaMapper::toResponse)
                .toList();
    }


    @Override
    public String evaluarAlerta(double temperatura, float precipitacion, String condicion) {
        if (temperatura > 35) {
            return "Temperatura crítica de " + temperatura + "°C detectada. Riesgo de estrés hídrico en cultivos.";
        }
        if (temperatura < 10) {
            return "Temperatura baja de " + temperatura + "°C detectada. Riesgo de heladas en cultivos.";
        }
        if (precipitacion > 50) {
            return "Precipitación alta de " + precipitacion + "mm detectada. Riesgo de inundación.";
        }
        if (condicion.contains("Tormenta")) {
            return "Tormenta detectada en la zona. Tome precauciones con los cultivos.";
        }
        return null; 
    }

    private Ubicacion obtenerUbicacion(Integer idUbicacion) {
        return ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + idUbicacion));
    }

    private String interpretarCodigo(int code) {
        return switch (code) {
            case 0          -> "Despejado";
            case 1          -> "Principalmente despejado";
            case 2          -> "Parcialmente nublado";
            case 3          -> "Nublado";
            case 45, 48     -> "Niebla";
            case 51, 53, 55 -> "Llovizna ligera";
            case 56, 57     -> "Llovizna helada";
            case 61, 63, 65 -> "Lluvia";
            case 66, 67     -> "Lluvia helada";
            case 71, 73, 75 -> "Nieve";
            case 77         -> "Granizo suelto";
            case 80, 81, 82 -> "Chubascos";
            case 85, 86     -> "Chubascos de nieve";
            case 95         -> "Tormenta";
            case 96, 99     -> "Tormenta con granizo";
            default         -> "Condición desconocida";
        };
    }
}