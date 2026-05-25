package com.agrotech.mapper;

import com.agrotech.Entity.Finca;
import com.agrotech.dto.request.FincaRequestDTO;
import com.agrotech.dto.request.FincaUpdateRequestDTO;
import com.agrotech.dto.response.FincaResponseDTO;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface FincaMapper {

    // FincaRequest -> Finca
    @Mapping(target = "idFinca", ignore = true) // Ignorar el ID al crear
    @Mapping(target = "productor", ignore = true) // Ignorar el productor, se asignará en el servicio
    @Mapping(target = "ubicacion", ignore = true)
    @Mapping(target = "siembras", ignore = true) // Ignorar las siembras, se gestionarán por separado
    @Mapping(target = "hectareas", expression = "java(toBigDecimal(fincaRequestDTO.getHectareas()))") // Convertir String a BigDecimal
    Finca toEntity(FincaRequestDTO fincaRequestDTO);

    // Finca -> FincaResponse
    @Mapping(source = "ubicacion.nombre", target = "nombreUbicacion") // Mapear el nombre de la ubicación
    @Mapping(source = "ubicacion.idUbicacion", target = "idUbicacion")
    @Mapping(target = "hectareas", expression = "java(toDouble(finca.getHectareas()))") // Convertir BigDecimal a String
    FincaResponseDTO toResponse(Finca finca);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idFinca", ignore = true) // Ignorar el ID al actualizar
    @Mapping(target = "productor", ignore = true) // Ignorar el productor,
    @Mapping(target = "ubicacion", ignore = true)
    @Mapping(target = "siembras", ignore = true)
    @Mapping(target = "hectareas", ignore = true)
    void updateEntityFromRequest(FincaUpdateRequestDTO fincaUpdateRequestDTO, @MappingTarget Finca finca);

    default void updateHectareas(FincaUpdateRequestDTO dto, Finca finca) {
        if (dto.getHectareas() != null) {
            finca.setHectareas(BigDecimal.valueOf(dto.getHectareas()));
        }
    }

    // Conversión Double -> BigDecimal
    default BigDecimal toBigDecimal(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    // Conversión BigDecimal -> Double
    default Double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
