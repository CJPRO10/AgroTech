package com.agrotech.mapper;

import com.agrotech.Entity.Anomalia;
import com.agrotech.dto.request.AnomaliaRequestDTO;
import com.agrotech.dto.response.AnomaliaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RecomendacionMapper.class})
public interface AnomaliaMapper {

    @Mapping(source = "idAnomalia",               target = "idAnomalia")
    @Mapping(source = "nombre",                   target = "nombre")
    @Mapping(source = "tipo",                     target = "tipo")
    @Mapping(source = "estado",                   target = "estado")
    @Mapping(source = "descripcion",              target = "descripcion")
    @Mapping(source = "nivelSeveridad",           target = "nivelSeveridad")
    @Mapping(source = "fechaDeteccion",           target = "fechaDeteccion")
    @Mapping(source = "siembra.idSiembra",        target = "idSiembra")
    @Mapping(source = "siembra.cultivo.nombre",   target = "nombreCultivo")
    @Mapping(source = "siembra.finca.nombreFinca",target = "nombreFinca")
    @Mapping(target = "recomendacion",            ignore = true) // se setea manualmente en el servicio
    AnomaliaResponseDTO toResponse(Anomalia anomalia);

    @Mapping(target = "idAnomalia",     ignore = true)
    @Mapping(target = "siembra",        ignore = true) // se setea manualmente en el servicio
    @Mapping(source = "nombre",         target = "nombre")
    @Mapping(source = "tipo",           target = "tipo")
    @Mapping(source = "estado",         target = "estado")
    @Mapping(source = "descripcion",    target = "descripcion")
    @Mapping(source = "nivelSeveridad", target = "nivelSeveridad")
    @Mapping(source = "fechaDeteccion", target = "fechaDeteccion")
    Anomalia toEntity(AnomaliaRequestDTO dto);
}
