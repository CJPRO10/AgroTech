package com.agrotech.repository;

import com.agrotech.Entity.Clima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClimaRepository extends JpaRepository<Clima, Integer> {

    // Historial por ubicacion
    List<Clima> findByUbicacion_IdUbicacionOrderByFechaMedicionDesc(Integer idUbicacion);

    // Rango de fechas para historial
    List<Clima> findByUbicacion_IdUbicacionAndFechaMedicionBetweenOrderByFechaMedicionDesc(
            Integer idUbicacion, LocalDate desde, LocalDate hasta);

    // ultima medicion registrada para una ubicacion para evitar duplicados
    Optional<Clima> findTopByUbicacion_IdUbicacionAndFechaMedicionOrderByFechaRegistroDesc(
            Integer idUbicacion, LocalDate fechaMedicion);
}
