package com.agrotech.service.util;

import com.agrotech.Entity.Anomalia;
import com.agrotech.Entity.Clima;
import com.agrotech.dto.response.ClimaResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromptBuilder {

    public String construirPromptRecomendacion(String categoria,
                                               String descripcionSolicitud,
                                               String estadoCultivo,
                                               List<String> anomaliasActivas,
                                               ClimaResponseDTO clima) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Eres un experto agrónomo. Genera una recomendación práctica y concisa ");
        prompt.append("(máximo 3 oraciones) para un productor agrícola colombiano. ");
        prompt.append("Responde solo con la recomendación, sin saludos ni explicaciones adicionales.\n\n");

        prompt.append("Información del cultivo:\n");
        prompt.append("- Categoría del problema: ").append(categoria).append("\n");
        prompt.append("- Estado actual del cultivo: ").append(estadoCultivo).append("\n");

        if (descripcionSolicitud != null && !descripcionSolicitud.isBlank()) {
            prompt.append("- Descripción del productor: ").append(descripcionSolicitud).append("\n");
        }

        if (!anomaliasActivas.isEmpty()) {
            prompt.append("- Anomalías activas: ").append(String.join(", ", anomaliasActivas)).append("\n");
        }

        if (clima != null) {
            prompt.append("- Condición climática actual: ").append(clima.getCondicion()).append("\n");
            prompt.append("- Temperatura: ").append(clima.getTemperatura()).append("°C\n");
            prompt.append("- Precipitación: ").append(clima.getPrecipitacion()).append(" mm\n");
        }

        return prompt.toString();
    }

    public String construirPromptDesdeAnomalia(Anomalia anomalia) {
        return """
        Eres un experto agrónomo. Genera una recomendación práctica y concisa \
        (máximo 3 oraciones) para un productor agrícola colombiano. \
        Responde solo con la recomendación, sin saludos ni explicaciones adicionales.
        
        Se detectó la siguiente anomalía en un cultivo:
        - Tipo: %s
        - Nivel de severidad: %s
        - Descripción: %s
        - Cultivo: %s
        """.formatted(
                anomalia.getTipo().name(),
                anomalia.getNivelSeveridad().name(),
                anomalia.getDescripcion() != null ? anomalia.getDescripcion() : "No especificada",
                anomalia.getSiembra().getCultivo().getNombre()
        );
    }

    public String construirPromptClimaGlobal(Clima clima, List<String> nombresCultivos) {
        return """
        Eres un experto agrónomo colombiano. Para cada cultivo listado, genera una \
        recomendación práctica de máximo 2 oraciones basada en las condiciones \
        climáticas actuales. Responde ÚNICAMENTE en este formato exacto, \
        una línea por cultivo, sin texto adicional:
        CULTIVO: [nombre] | RECOMENDACION: [texto]
        
        Condiciones climáticas:
        - Condición: %s
        - Temperatura: %s°C
        - Precipitación: %s mm
        - Ubicación: %s
        
        Cultivos a analizar: %s
        """.formatted(
                clima.getCondicion(),
                clima.getTemperatura(),
                clima.getPrecipitacion(),
                clima.getUbicacion().getNombre(),
                String.join(", ", nombresCultivos)
        );
    }
}