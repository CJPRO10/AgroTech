package com.agrotech.repository;

import com.agrotech.Entity.PreferenciaNotificacion;
import com.agrotech.Entity.enums.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreferenciaNotificacionRepository extends JpaRepository<PreferenciaNotificacion, Integer> {

    List<PreferenciaNotificacion> findByUsuario_IdUsuario(Integer idUsuario);
    Optional<PreferenciaNotificacion> findByUsuario_IdUsuarioAndTipoAlerta(Integer idUsuario, TipoNotificacion tipoAlerta);
}