package com.agrotech.mapper;

import com.agrotech.Entity.Recomendacion;
import com.agrotech.dto.response.RecomendacionResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecomendacionMapper {

    @Mapping(source = "idRecomendacion", target = "idRecomendacion")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "prioridad", target = "prioridad")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "fechaGeneracion", target = "fechaGeneracion")
    @Mapping(source = "siembra.idSiembra", target = "idSiembra")
    @Mapping(source = "anomalia.idAnomalia", target = "idAnomalia")
    @Mapping(source = "clima.idClima",       target = "idClima")
    RecomendacionResponseDTO toResponse(Recomendacion recomendacion);

}
