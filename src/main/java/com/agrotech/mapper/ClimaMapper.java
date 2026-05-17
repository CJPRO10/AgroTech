package com.agrotech.mapper;

import com.agrotech.Entity.Clima;
import com.agrotech.dto.response.ClimaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClimaMapper {

    @Mapping(source = "idClima",              target = "idClima")
    @Mapping(source = "condicion",            target = "condicion")
    @Mapping(source = "temperatura",          target = "temperatura")
    @Mapping(source = "precipitacion",        target = "precipitacion")
    @Mapping(source = "fechaMedicion",        target = "fechaMedicion")
    @Mapping(source = "ubicacion.nombre",     target = "nombreUbicacion")
    @Mapping(source = "ubicacion.idUbicacion",target = "idUbicacion")
    @Mapping(target = "alerta",               ignore = true)
    ClimaResponseDTO toResponse(Clima clima);
}