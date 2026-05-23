package com.agrotech.repository;

import com.agrotech.Entity.EjecucionTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EjecucionTareaRepository extends JpaRepository<EjecucionTarea, Integer> {

    List<EjecucionTarea> findByOperario_IdUsuario(Integer idOperario);
    List<EjecucionTarea> findByAuxiliar_IdUsuario(Integer idAuxiliar);
    List<EjecucionTarea> findByTarea_IdTarea(Integer idTarea);
    List<EjecucionTarea> findByCreadoPor_IdUsuario(Integer idUsuario);
}