package com.agrotech.repository;

import com.agrotech.Entity.Recomendacion;
import com.agrotech.Entity.enums.EstadoRecomendacion;
import com.agrotech.Entity.enums.PrioridadRecomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, Integer> {

    List<Recomendacion> findByFechaGeneracionBetween(LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT r FROM Recomendacion r ORDER BY r.fechaGeneracion DESC")
    List<Recomendacion> findAllOrderByFechaDesc();

    List<Recomendacion> findByCategoriaIgnoreCase(String categoria);

    List<Recomendacion> findByPrioridad(PrioridadRecomendacion prioridad);

    List<Recomendacion> findByEstado(EstadoRecomendacion estado);

    List<Recomendacion> findByAnomalia_IdAnomalia(Integer idAnomalia);

    List<Recomendacion> findBySiembra_IdSiembra(Integer idSiembra);
}
