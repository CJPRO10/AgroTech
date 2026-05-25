package com.agrotech.service.impl;

import com.agrotech.Entity.Finanza;
import com.agrotech.Entity.Productor;
import com.agrotech.Entity.Usuario;
import com.agrotech.dto.request.FinanzaRequestDTO;
import com.agrotech.dto.response.FinanzaResponseDTO;
import com.agrotech.repository.FinanzaRepository;
import com.agrotech.repository.UsuarioRepository;
import com.agrotech.service.FinanzaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class FinanzaServiceImpl implements FinanzaService {

    private final FinanzaRepository finanzaRepository;
    private final UsuarioRepository usuarioRepository;

    public FinanzaServiceImpl(FinanzaRepository finanzaRepository,
                              UsuarioRepository usuarioRepository) {
        this.finanzaRepository = finanzaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Productor obtenerProductor(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!(usuario instanceof Productor productor)) {
            throw new RuntimeException("Solo los productores pueden gestionar finanzas");
        }
        return productor;
    }

    private FinanzaResponseDTO toResponse(Finanza f) {
        FinanzaResponseDTO dto = new FinanzaResponseDTO();
        dto.setIdFinanza(f.getIdFinanza());
        dto.setDescripcion(f.getDescripcion());
        dto.setMonto(f.getMonto());
        dto.setTipoTransaccion(f.getTipoTransaccion());
        dto.setCategoria(f.getCategoria());
        dto.setFechaRegistro(f.getFechaRegistro());
        dto.setFechaActualizacion(f.getFechaActualizacion());
        return dto;
    }

    @Override
    public FinanzaResponseDTO crear(String correo, FinanzaRequestDTO dto) {
        Productor productor = obtenerProductor(correo);
        Finanza finanza = new Finanza();
        finanza.setDescripcion(dto.getDescripcion());
        finanza.setMonto(dto.getMonto());
        finanza.setTipoTransaccion(dto.getTipoTransaccion().toUpperCase());
        finanza.setCategoria(dto.getCategoria().toUpperCase());
        finanza.setFechaRegistro(LocalDateTime.now());
        finanza.setFechaActualizacion(LocalDateTime.now());
        finanza.setUsuarioProductor(productor);
        return toResponse(finanzaRepository.save(finanza));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanzaResponseDTO> listar(String correo) {
        Productor productor = obtenerProductor(correo);
        return finanzaRepository.findByUsuarioProductor_IdUsuario(productor.getIdUsuario())
                .stream().map(this::toResponse).toList();
    }

    @Override
    public FinanzaResponseDTO actualizar(String correo, Integer idFinanza, FinanzaRequestDTO dto) {
        Productor productor = obtenerProductor(correo);
        Finanza finanza = finanzaRepository.findById(idFinanza)
                .orElseThrow(() -> new RuntimeException("Finanza no encontrada"));
        if (!finanza.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario())) {
            throw new RuntimeException("No tienes permiso para modificar este registro");
        }
        if (dto.getDescripcion() != null) finanza.setDescripcion(dto.getDescripcion());
        if (dto.getMonto() != null) finanza.setMonto(dto.getMonto());
        if (dto.getTipoTransaccion() != null) finanza.setTipoTransaccion(dto.getTipoTransaccion().toUpperCase());
        if (dto.getCategoria() != null) finanza.setCategoria(dto.getCategoria().toUpperCase());
        finanza.setFechaActualizacion(LocalDateTime.now());
        return toResponse(finanzaRepository.save(finanza));
    }

    @Override
    public void eliminar(String correo, Integer idFinanza) {
        Productor productor = obtenerProductor(correo);
        Finanza finanza = finanzaRepository.findById(idFinanza)
                .orElseThrow(() -> new RuntimeException("Finanza no encontrada"));
        if (!finanza.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario())) {
            throw new RuntimeException("No tienes permiso para eliminar este registro");
        }
        finanzaRepository.deleteById(idFinanza);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanzaResponseDTO> filtrarPorTipo(String correo, String tipo) {
        Productor productor = obtenerProductor(correo);
        return finanzaRepository.findByProductorAndTipo(productor.getIdUsuario(), tipo.toUpperCase())
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinanzaResponseDTO> filtrarPorCategoria(String correo, String categoria) {
        Productor productor = obtenerProductor(correo);
        return finanzaRepository.findByCategoriaIgnoreCase(categoria)
                .stream()
                .filter(f -> f.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario()))
                .map(this::toResponse).toList();
    }
}
