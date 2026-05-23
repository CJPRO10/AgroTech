package com.agrotech.mapper;

import com.agrotech.Entity.Notificacion;
import com.agrotech.dto.response.NotificacionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {

    @Mapping(source = "idNotificacion", target = "idNotificacion")
    @Mapping(source = "titulo", target = "titulo")
    @Mapping(source = "mensaje", target = "mensaje")
    @Mapping(source = "tipo", target = "tipo")
    @Mapping(source = "prioridad", target = "prioridad")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "fechaCreacion", target = "fechaCreacion")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    NotificacionResponseDTO toResponse(Notificacion notificacion);
}