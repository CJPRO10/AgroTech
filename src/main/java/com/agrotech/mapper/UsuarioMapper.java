package com.agrotech.mapper;

import com.agrotech.Entity.Usuario;
import com.agrotech.dto.request.UsuarioRequestDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "rol.nombre", target = "rol")
    @Mapping(source = "activo", target = "activo")
    UsuarioResponseDTO toResponse(Usuario usuario);

    @Mapping(target = "idUsuario",       ignore = true)
    @Mapping(target = "rol",             ignore = true)
    @Mapping(target = "contrasena",      ignore = true)
    @Mapping(target = "activo",          ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUsuario",       ignore = true)
    @Mapping(target = "rol",             ignore = true)
    @Mapping(target = "contrasena",      ignore = true)
    @Mapping(target = "activo",          ignore = true)
    void updateEntityFromRequest(UsuarioRequestDTO dto, @MappingTarget Usuario usuario);

}