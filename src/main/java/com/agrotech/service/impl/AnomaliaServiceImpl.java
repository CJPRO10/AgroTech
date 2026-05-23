package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.Entity.enums.*;
import com.agrotech.dto.request.AnomaliaRequestDTO;
import com.agrotech.dto.response.AnomaliaResponseDTO;
import com.agrotech.mapper.AnomaliaMapper;
import com.agrotech.mapper.RecomendacionMapper;
import com.agrotech.repository.*;
import com.agrotech.service.AnomaliaService;
import com.agrotech.service.NotificacionService;
import com.agrotech.service.util.PromptBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AnomaliaServiceImpl implements AnomaliaService {

    private final AnomaliaRepository anomaliaRepository;
    private final RecomendacionRepository recomendacionRepository;
    private final SiembraRepository siembraRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnomaliaMapper anomaliaMapper;
    private final RecomendacionMapper recomendacionMapper;
    private final GeminiService geminiService;
    private final PromptBuilder promptBuilder;
    private final OperarioRepository operarioRepository;
    private final AuxiliarRepository auxiliarRepository;
    private final NotificacionService notificacionService;

    public AnomaliaServiceImpl(AnomaliaRepository anomaliaRepository,
                               RecomendacionRepository recomendacionRepository,
                               SiembraRepository siembraRepository,
                               UsuarioRepository usuarioRepository,
                               AnomaliaMapper anomaliaMapper,
                               RecomendacionMapper recomendacionMapper,
                               GeminiService geminiService,
                               PromptBuilder promptBuilder,
                               OperarioRepository operarioRepository,
                               AuxiliarRepository auxiliarRepository,
                               NotificacionService notificacionService) {
        this.anomaliaRepository = anomaliaRepository;
        this.recomendacionRepository = recomendacionRepository;
        this.siembraRepository = siembraRepository;
        this.usuarioRepository = usuarioRepository;
        this.anomaliaMapper = anomaliaMapper;
        this.recomendacionMapper = recomendacionMapper;
        this.geminiService = geminiService;
        this.promptBuilder = promptBuilder;
        this.operarioRepository = operarioRepository;
        this.auxiliarRepository = auxiliarRepository;
        this.notificacionService = notificacionService;
    }

    @Override
    public AnomaliaResponseDTO registrar(AnomaliaRequestDTO dto, String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Siembra siembra = siembraRepository.findById(dto.getIdSiembra())
                .orElseThrow(() -> new RuntimeException("Siembra no encontrada: " + dto.getIdSiembra()));

        Anomalia anomalia = anomaliaMapper.toEntity(dto);
        anomalia.setSiembra(siembra);
        anomalia.setRegistradoPor(usuario);
        anomalia = anomaliaRepository.save(anomalia);
        anomalia.setSiembra(siembra);
        anomalia.setRegistradoPor(usuario);

        Recomendacion recomendacion = construirRecomendacion(anomalia, siembra);
        recomendacion = recomendacionRepository.save(recomendacion);

        AnomaliaResponseDTO response = anomaliaMapper.toResponse(anomalia);
        response.setRegistradoPor(usuario.getNombre() + " " + usuario.getApellido());
        response.setRecomendacion(recomendacionMapper.toResponse(recomendacion));
        notificacionService.generarNotificacionAnomalia(anomalia);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnomaliaResponseDTO> listar(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rol = usuario.getRol().getNombre();

        if ("PRODUCTOR".equals(rol)) {
            return anomaliaRepository.findAll().stream()
                    .filter(a -> a.getSiembra().getFinca()
                            .getProductor().getIdUsuario().equals(usuario.getIdUsuario()))
                    .map(this::buildResponse)
                    .toList();
        }

        if ("OPERARIO".equals(rol)) {
            Operario operario = operarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Operario no encontrado"));
            if (operario.getProductor() == null) return List.of();
            Integer idProductor = operario.getProductor().getIdUsuario();
            return anomaliaRepository.findAll().stream()
                    .filter(a -> a.getSiembra().getFinca()
                            .getProductor().getIdUsuario().equals(idProductor))
                    .map(this::buildResponse)
                    .toList();
        }

        if ("AUXILIAR".equals(rol)) {
            Auxiliar auxiliar = auxiliarRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Auxiliar no encontrado"));
            if (auxiliar.getProductor() == null) return List.of();
            Integer idProductor = auxiliar.getProductor().getIdUsuario();
            return anomaliaRepository.findAll().stream()
                    .filter(a -> a.getSiembra().getFinca()
                            .getProductor().getIdUsuario().equals(idProductor))
                    .map(this::buildResponse)
                    .toList();
        }

        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnomaliaResponseDTO> listarPorSiembra(Integer idSiembra, String correo) {
        return anomaliaRepository.findBySiembra_IdSiembra(idSiembra).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnomaliaResponseDTO> listarPorTipo(TipoAnomalia tipo, String correo) {
        return anomaliaRepository.findByTipo(tipo).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnomaliaResponseDTO> listarPorEstado(EstadoAnomalia estado, String correo) {
        return anomaliaRepository.findByEstado(estado).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnomaliaResponseDTO> listarPorNivelSeveridad(NivelSeveridad nivelSeveridad, String correo) {
        return anomaliaRepository.findByNivelSeveridad(nivelSeveridad).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnomaliaResponseDTO> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta, String correo) {
        return anomaliaRepository.findByFechaDeteccionBetween(desde, hasta).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AnomaliaResponseDTO buscarPorId(Integer idAnomalia, String correo) {
        Anomalia anomalia = anomaliaRepository.findById(idAnomalia)
                .orElseThrow(() -> new RuntimeException("Anomalía no encontrada: " + idAnomalia));
        return buildResponse(anomalia);
    }

    @Override
    public AnomaliaResponseDTO actualizar(Integer idAnomalia, AnomaliaRequestDTO dto, String correo) {
        Anomalia anomalia = anomaliaRepository.findById(idAnomalia)
                .orElseThrow(() -> new RuntimeException("Anomalía no encontrada: " + idAnomalia));

        if (dto.getNombre() != null) anomalia.setNombre(dto.getNombre());
        if (dto.getTipo() != null) anomalia.setTipo(dto.getTipo());
        if (dto.getEstado() != null) anomalia.setEstado(dto.getEstado());
        if (dto.getDescripcion() != null) anomalia.setDescripcion(dto.getDescripcion());
        if (dto.getNivelSeveridad() != null) anomalia.setNivelSeveridad(dto.getNivelSeveridad());
        if (dto.getFechaDeteccion() != null) anomalia.setFechaDeteccion(dto.getFechaDeteccion());

        Usuario registradoPor = anomalia.getRegistradoPor();
        Siembra siembra = anomalia.getSiembra();

        anomalia = anomaliaRepository.save(anomalia);
        anomalia.setRegistradoPor(registradoPor);
        anomalia.setSiembra(siembra);

        Recomendacion recomendacion = construirRecomendacion(anomalia, anomalia.getSiembra());
        recomendacion = recomendacionRepository.save(recomendacion);

        AnomaliaResponseDTO response = anomaliaMapper.toResponse(anomalia);

        if (registradoPor != null) {
            response.setRegistradoPor(registradoPor.getNombre() + " " + registradoPor.getApellido());
        }

        response.setRecomendacion(recomendacionMapper.toResponse(recomendacion));
        notificacionService.generarNotificacionAnomalia(anomalia);
        return response;
    }

    @Override
    public void eliminar(Integer idAnomalia, String correo) {
        if (!anomaliaRepository.existsById(idAnomalia)) {
            throw new RuntimeException("Anomalía no encontrada: " + idAnomalia);
        }
        anomaliaRepository.deleteById(idAnomalia);
    }

    private AnomaliaResponseDTO buildResponse(Anomalia anomalia) {
        AnomaliaResponseDTO response = anomaliaMapper.toResponse(anomalia);

        if (anomalia.getRegistradoPor() != null) {
            response.setRegistradoPor(
                    anomalia.getRegistradoPor().getNombre() + " " +
                            anomalia.getRegistradoPor().getApellido()
            );
        }

        recomendacionRepository.findByAnomalia_IdAnomalia(anomalia.getIdAnomalia())
                .stream()
                .findFirst()
                .ifPresent(rec -> response.setRecomendacion(recomendacionMapper.toResponse(rec)));
        return response;
    }

    private Recomendacion construirRecomendacion(Anomalia anomalia, Siembra siembra) {
        String prompt = promptBuilder.construirPromptDesdeAnomalia(anomalia);
        String descripcion = geminiService.generarRecomendacion(prompt);

        Recomendacion rec = new Recomendacion();
        rec.setDescripcion(descripcion);
        rec.setPrioridad(determinarPrioridad(anomalia.getNivelSeveridad()));
        rec.setCategoria(anomalia.getTipo().name());
        rec.setEstado(EstadoRecomendacion.PENDIENTE);
        rec.setFechaGeneracion(LocalDateTime.now());
        rec.setAnomalia(anomalia);
        rec.setSiembra(siembra);
        return rec;
    }

    private PrioridadRecomendacion determinarPrioridad(NivelSeveridad nivelSeveridad) {
        if (nivelSeveridad == null) return PrioridadRecomendacion.BAJA;
        return switch (nivelSeveridad) {
            case CRITICA, ALTA -> PrioridadRecomendacion.ALTA;
            case MEDIA         -> PrioridadRecomendacion.MEDIA;
            case BAJA          -> PrioridadRecomendacion.BAJA;
        };
    }
}