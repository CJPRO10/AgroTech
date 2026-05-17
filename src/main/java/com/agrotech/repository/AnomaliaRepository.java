package com.agrotech.repository;

import com.agrotech.Entity.Anomalia;
import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.NivelSeveridad;
import com.agrotech.Entity.enums.TipoAnomalia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnomaliaRepository extends JpaRepository<Anomalia, Integer> {

    List<Anomalia> findBySiembra_IdSiembra(Integer idSiembra);

   // List<Anomalia> findByUsuario_IdUsuario(Integer idUsuario);

    List<Anomalia> findByTipo(TipoAnomalia tipo);

    List<Anomalia> findByEstado(EstadoAnomalia estado);

    List<Anomalia> findByNivelSeveridad(NivelSeveridad nivelSeveridad);

    List<Anomalia> findByFechaDeteccionBetween(LocalDateTime desde, LocalDateTime hasta);

    @Query("SELECT a FROM Anomalia a WHERE a.siembra.idSiembra = :idSiembra " +
            "AND a.estado = :estado ORDER BY a.fechaDeteccion DESC")
    List<Anomalia> findBySiembraAndEstado(@Param("idSiembra") Integer idSiembra,
                                          @Param("estado") EstadoAnomalia estado);

}
