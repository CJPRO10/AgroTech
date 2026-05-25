package com.agrotech.service.impl;

import com.agrotech.Entity.Finca;
import com.agrotech.Entity.Productor;
import com.agrotech.Entity.Ubicacion;
import com.agrotech.Entity.Usuario;
import com.agrotech.dto.request.FincaRequestDTO;
import com.agrotech.dto.request.FincaUpdateRequestDTO;
import com.agrotech.dto.response.FincaResponseDTO;
import com.agrotech.mapper.FincaMapper;
import com.agrotech.repository.FincaRepository;
import com.agrotech.repository.ProductorRepository;
import com.agrotech.repository.UbicacionRepository;
import com.agrotech.repository.UsuarioRepository;
import com.agrotech.service.FincaServIce;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FincaServIceImpl implements FincaServIce {

    private final FincaRepository fincaRepository;
    private final ProductorRepository productorRepository;
    private final UbicacionRepository ubicacionRepository;
    private final FincaMapper fincaMapper;
    private final UsuarioRepository usuarioRepository;

    public FincaServIceImpl(FincaRepository fincaRepository,
                            ProductorRepository productorRepository,
                            UbicacionRepository ubicacionRepository,
                            FincaMapper fincaMapper,
                            UsuarioRepository usuarioRepository) {
        this.fincaRepository = fincaRepository;
        this.productorRepository = productorRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.fincaMapper = fincaMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<FincaResponseDTO> listarPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // PRODUCTOR: sus propias fincas
        if (usuario instanceof Productor productor) {
            return fincaRepository.findByProductor_IdUsuario(productor.getIdUsuario())
                    .stream()
                    .map(fincaMapper::toResponse)
                    .toList();
        }

        // OPERARIO: fincas del productor al que pertenece
        if (usuario instanceof com.agrotech.Entity.Operario operario) {
            if (operario.getProductor() == null) return List.of();
            return fincaRepository.findByProductor_IdUsuario(operario.getProductor().getIdUsuario())
                    .stream()
                    .map(fincaMapper::toResponse)
                    .toList();
        }

        // AUXILIAR: fincas del productor al que pertenece
        if (usuario instanceof com.agrotech.Entity.Auxiliar auxiliar) {
            if (auxiliar.getProductor() == null) return List.of();
            return fincaRepository.findByProductor_IdUsuario(auxiliar.getProductor().getIdUsuario())
                    .stream()
                    .map(fincaMapper::toResponse)
                    .toList();
        }

        return List.of();
    }

    @Override
    public List<FincaResponseDTO> buscarPorNombre(String nombre, String correo) {
        Productor productor = obtenerProductor(correo);
        return fincaRepository.findByProductor_IdUsuario(productor.getIdUsuario())
                .stream()
                .filter(f -> f.getNombreFinca().toLowerCase().contains(nombre.toLowerCase()))
                .toList()
                .stream()
                .map(fincaMapper::toResponse)
                .toList();
    }

    @Override
    public FincaResponseDTO crear(String correo, FincaRequestDTO dto) {
        Productor productor = obtenerProductor(correo);
        Ubicacion ubicacion = ubicacionRepository.findById(dto.getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada: " + dto.getIdUbicacion()));

        boolean existeFinca = fincaRepository.existsByNombreFincaAndProductor_IdUsuario(
                dto.getNombreFinca(), productor.getIdUsuario());
        if (existeFinca) {
            throw new RuntimeException("Ya existe una finca con ese nombre");
        }

        Finca finca = fincaMapper.toEntity(dto);
        finca.setProductor(productor);
        finca.setUbicacion(ubicacion);
        return fincaMapper.toResponse(fincaRepository.save(finca));
    }

    @Override
    public FincaResponseDTO actualizar(Integer idFinca, String correo, FincaUpdateRequestDTO dto) {
        Productor productor = obtenerProductor(correo);
        Finca finca = fincaRepository.findById(idFinca)
                .orElseThrow(() -> new RuntimeException("Finca no encontrada: " + idFinca));
        validarPropietario(finca, productor);

        if (dto.getNombreFinca() != null && !dto.getNombreFinca().equalsIgnoreCase(finca.getNombreFinca())) {
            if (fincaRepository.existsByNombreFincaAndProductor_IdUsuario(
                    dto.getNombreFinca(), productor.getIdUsuario())) {
                throw new RuntimeException("Ya existe una finca con ese nombre");
            }
        }

        fincaMapper.updateEntityFromRequest(dto, finca);
        fincaMapper.updateHectareas(dto, finca);
        return fincaMapper.toResponse(fincaRepository.save(finca));
    }

    @Override
    public void eliminar(Integer idFinca, String correo) {
        Productor productor = obtenerProductor(correo);
        Finca finca = fincaRepository.findById(idFinca)
                .orElseThrow(() -> new RuntimeException("Finca no encontrada: " + idFinca));
        validarPropietario(finca, productor);
        fincaRepository.delete(finca);
    }

    private Productor obtenerProductor(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!(usuario instanceof Productor productor)) {
            throw new RuntimeException("El usuario no es un Productor");
        }
        return productor;
    }

    private void validarPropietario(Finca finca, Productor productor) {
        if (!finca.getProductor().getIdUsuario().equals(productor.getIdUsuario())) {
            throw new RuntimeException("No tienes permiso para acceder a esta finca");
        }
    }
}
