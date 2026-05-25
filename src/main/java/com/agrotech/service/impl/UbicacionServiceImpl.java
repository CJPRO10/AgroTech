package com.agrotech.service.impl;

import com.agrotech.Entity.Ubicacion;
import com.agrotech.dto.request.UbicacionRequestDTO;
import com.agrotech.repository.UbicacionRepository;
import com.agrotech.service.UbicacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private static final double RADIO_KM = 0.5; // 500 metros

    private final UbicacionRepository ubicacionRepository;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ubicacion> listar() {
        return ubicacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ubicacion> buscarPorNombre(String nombre) {
        return ubicacionRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Ubicacion crearOReusar(UbicacionRequestDTO dto) {
        // Buscar si ya existe una ubicación muy cercana (< 500m)
        List<Ubicacion> existentes = ubicacionRepository.findAll();
        for (Ubicacion u : existentes) {
            if (calcularDistanciaKm(u.getLatitud(), u.getLongitud(),
                    dto.getLatitud(), dto.getLongitud()) < RADIO_KM) {
                return u; // reutilizar la existente
            }
        }

        // Crear nueva ubicación
        Ubicacion nueva = new Ubicacion();
        nueva.setNombre(dto.getNombre() != null && !dto.getNombre().isBlank()
                ? dto.getNombre()
                : "Ubicación personalizada");
        nueva.setLatitud(dto.getLatitud());
        nueva.setLongitud(dto.getLongitud());
        return ubicacionRepository.save(nueva);
    }

    // Fórmula de Haversine — distancia en km entre dos coordenadas
    private double calcularDistanciaKm(double lat1, double lon1,
                                       double lat2, double lon2) {
        final double R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}