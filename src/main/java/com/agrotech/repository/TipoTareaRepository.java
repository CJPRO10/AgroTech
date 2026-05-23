package com.agrotech.repository;

import com.agrotech.Entity.TipoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoTareaRepository extends JpaRepository<TipoTarea, Integer> {
    Optional<TipoTarea> findByNombre(String nombre);
    List<TipoTarea> findByNombreContainingIgnoreCase(String nombre);
}