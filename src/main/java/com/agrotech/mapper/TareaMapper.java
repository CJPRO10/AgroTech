package com.agrotech.mapper;

import com.agrotech.Entity.Tarea;
import com.agrotech.dto.response.TareaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EjecucionTareaMapper.class})
public interface TareaMapper {

    @Mapping(source = "idTarea", target = "idTarea")
    @Mapping(source = "tipoTarea.nombre", target = "tipoTarea")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "fechaLimite", target = "fechaLimite")
    @Mapping(source = "siembra.idSiembra", target = "idSiembra")
    @Mapping(source = "siembra.cultivo.nombre", target = "nombreSiembra")
    @Mapping(target = "asignaciones", ignore = true)
    TareaResponseDTO toResponse(Tarea tarea);
}