package com.agrotech.mapper;

import com.agrotech.Entity.EjecucionTarea;
import com.agrotech.dto.response.EjecucionTareaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EjecucionTareaMapper {

    @Mapping(source = "idEjecucionTarea", target = "idEjecucion")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "fechaEstado", target = "fechaEstado")
    @Mapping(source = "fechaLimite", target = "fechaLimite")
    @Mapping(source = "creadoPor.nombre", target = "creadoPor")
    @Mapping(source = "operario.nombre", target = "operarioAsignado")
    @Mapping(source = "auxiliar.nombre", target = "auxiliarAsignado")
    EjecucionTareaResponseDTO toResponse(EjecucionTarea ejecucionTarea);
}