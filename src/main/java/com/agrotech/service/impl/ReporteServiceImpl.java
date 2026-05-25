package com.agrotech.service.impl;

import com.agrotech.Entity.Productor;
import com.agrotech.Entity.Reporte;
import com.agrotech.Entity.Usuario;
import com.agrotech.dto.request.ReporteRequestDTO;
import com.agrotech.dto.response.ReporteResponseDTO;
import com.agrotech.repository.ReporteRepository;
import com.agrotech.repository.UsuarioRepository;
import com.agrotech.service.ReporteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteServiceImpl(ReporteRepository reporteRepository,
                              UsuarioRepository usuarioRepository) {
        this.reporteRepository = reporteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private Productor obtenerProductor(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!(usuario instanceof Productor productor)) {
            throw new RuntimeException("Solo los productores pueden gestionar reportes");
        }
        return productor;
    }

    private ReporteResponseDTO toResponse(Reporte r) {
        ReporteResponseDTO dto = new ReporteResponseDTO();
        dto.setIdReporte(r.getIdReporte());
        dto.setNombreReporte(r.getNombreReporte());
        dto.setFormato(r.getFormato());
        dto.setTipoPeriodicidad(r.getTipoPeriodicidad());
        dto.setFechaCreacion(r.getFechaCreacion());
        if (r.getUsuarioProductor() != null) {
            dto.setNombreProductor(r.getUsuarioProductor().getNombre()
                    + " " + r.getUsuarioProductor().getApellido());
        }
        return dto;
    }

    @Override
    public ReporteResponseDTO crear(String correo, ReporteRequestDTO dto) {
        Productor productor = obtenerProductor(correo);
        Reporte reporte = new Reporte();
        reporte.setNombreReporte(dto.getNombreReporte());
        reporte.setFormato(dto.getFormato());
        reporte.setTipoPeriodicidad(dto.getTipoPeriodicidad());
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setUsuarioProductor(productor);
        return toResponse(reporteRepository.save(reporte));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> listar(String correo) {
        Productor productor = obtenerProductor(correo);
        return reporteRepository
                .findByProductorOrderByFechaDesc(productor.getIdUsuario())
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> buscarPorNombre(String correo, String nombre) {
        Productor productor = obtenerProductor(correo);
        return reporteRepository.findByNombreReporteContainingIgnoreCase(nombre)
                .stream()
                .filter(r -> r.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario()))
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> filtrarPorFormato(String correo, String formato) {
        Productor productor = obtenerProductor(correo);
        return reporteRepository.findByFormatoIgnoreCase(formato)
                .stream()
                .filter(r -> r.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario()))
                .map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponseDTO> filtrarPorPeriodicidad(String correo, String periodicidad) {
        Productor productor = obtenerProductor(correo);
        return reporteRepository.findByTipoPeriodicidadIgnoreCase(periodicidad)
                .stream()
                .filter(r -> r.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario()))
                .map(this::toResponse).toList();
    }

    @Override
    public void eliminar(String correo, Integer idReporte) {
        Productor productor = obtenerProductor(correo);
        Reporte reporte = reporteRepository.findById(idReporte)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        if (!reporte.getUsuarioProductor().getIdUsuario().equals(productor.getIdUsuario())) {
            throw new RuntimeException("No tienes permiso para eliminar este reporte");
        }
        reporteRepository.deleteById(idReporte);
    }
}
