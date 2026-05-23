package com.agrotech.repository;

import com.agrotech.Entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Integer> {

    @Query("SELECT t FROM Tarea t WHERE t.siembra.finca.productor.idUsuario = :idProductor")
    List<Tarea> findByProductor(@Param("idProductor") Integer idProductor);

    List<Tarea> findBySiembra_IdSiembra(Integer idSiembra);
}