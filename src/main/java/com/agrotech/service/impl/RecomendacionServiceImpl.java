package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.Entity.enums.EstadoAnomalia;
import com.agrotech.Entity.enums.EstadoRecomendacion;
import com.agrotech.Entity.enums.PrioridadRecomendacion;
import com.agrotech.dto.request.RecomendacionRequestDTO;
import com.agrotech.dto.response.ClimaResponseDTO;
import com.agrotech.dto.response.RecomendacionResponseDTO;
import com.agrotech.mapper.RecomendacionMapper;
import com.agrotech.repository.*;
import com.agrotech.service.ClimaService;
import com.agrotech.service.RecomendacionService;
import com.agrotech.service.util.PromptBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecomendacionServiceImpl implements RecomendacionService {

    private final RecomendacionRepository recomendacionRepository;
    private final SiembraRepository siembraRepository;
    private final RecomendacionMapper recomendacionMapper;
    private final AnomaliaRepository anomaliaRepository;
    private final SiembraEstadoCultivoRepository siembraEstadoCultivoRepository;
    private final ClimaService climaService;
    private final GeminiService geminiService;
    private final PromptBuilder promptBuilder;
    private final ClimaRepository climaRepository;
    private final UsuarioRepository usuarioRepository;
    private final OperarioRepository operarioRepository;

    public RecomendacionServiceImpl(RecomendacionRepository recomendacionRepository,
                                    SiembraRepository siembraRepository,
                                    RecomendacionMapper recomendacionMapper,
                                    AnomaliaRepository anomaliaRepository,
                                    SiembraEstadoCultivoRepository siembraEstadoCultivoRepository,
                                    ClimaService climaService,
                                    GeminiService geminiService,PromptBuilder promptBuilder,
                                    ClimaRepository climaRepository,
                                    UsuarioRepository usuarioRepository,
                                    OperarioRepository operarioRepository) {
        this.recomendacionRepository = recomendacionRepository;
        this.siembraRepository = siembraRepository;
        this.recomendacionMapper = recomendacionMapper;
        this.anomaliaRepository = anomaliaRepository;
        this.siembraEstadoCultivoRepository = siembraEstadoCultivoRepository;
        this.climaService = climaService;
        this.geminiService = geminiService;
        this.promptBuilder = promptBuilder;
        this.climaRepository = climaRepository;
        this.usuarioRepository = usuarioRepository;
        this.operarioRepository = operarioRepository;
    }

    @Override
    public RecomendacionResponseDTO solicitar(RecomendacionRequestDTO dto) {

        Siembra siembra = siembraRepository.findById(dto.getIdSiembra())
                .orElseThrow(() -> new RuntimeException("Siembra no encontrada con ID: " + dto.getIdSiembra()));

        String estadoActual = obtenerEstadoActualCultivo(siembra.getIdSiembra());

        List<String> anomaliasActivas = obtenerAnomaliasActivas(siembra.getIdSiembra());

        ClimaResponseDTO clima = null;
        if (siembra.getFinca() != null && siembra.getFinca().getUbicacion() != null) {
            try {
                clima = climaService.consultarClimaActual(siembra.getFinca().getUbicacion().getIdUbicacion());
            } catch (Exception e) {
                clima = null;
            }
        }

        String prompt = promptBuilder.construirPromptRecomendacion(
                dto.getCategoria(),
                dto.getDescripcionSolicitud(),
                estadoActual,
                anomaliasActivas,
                clima
        );

        String textoRecomendacion = geminiService.generarRecomendacion(prompt);

        PrioridadRecomendacion prioridad = determinarPrioridadManual(!anomaliasActivas.isEmpty(), false);

        Recomendacion recomendacion = new Recomendacion();
        recomendacion.setDescripcion(textoRecomendacion);
        recomendacion.setCategoria(dto.getCategoria());
        recomendacion.setPrioridad(prioridad);
        recomendacion.setEstado(EstadoRecomendacion.PENDIENTE);
        recomendacion.setFechaGeneracion(LocalDateTime.now());
        recomendacion.setSiembra(siembra);
        recomendacion.setAnomalia(null);

        recomendacion = recomendacionRepository.save(recomendacion);
        return recomendacionMapper.toResponse(recomendacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listar(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rol = usuario.getRol().getNombre();

        if ("PRODUCTOR".equals(rol)) {
            return recomendacionRepository.findAllOrderByFechaDesc().stream()
                    .filter(r -> r.getSiembra() != null &&
                            r.getSiembra().getFinca().getProductor()
                                    .getIdUsuario().equals(usuario.getIdUsuario()))
                    .map(recomendacionMapper::toResponse)
                    .toList();
        }

        if ("OPERARIO".equals(rol)) {
            Operario operario = operarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Operario no encontrado"));
            if (operario.getProductor() == null) return List.of();
            Integer idProductor = operario.getProductor().getIdUsuario();
            return recomendacionRepository.findAllOrderByFechaDesc().stream()
                    .filter(r -> r.getSiembra() != null &&
                            r.getSiembra().getFinca().getProductor()
                                    .getIdUsuario().equals(idProductor))
                    .map(recomendacionMapper::toResponse)
                    .toList();
        }

        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listarPorSiembra(Integer idSiembra) {
        return recomendacionRepository.findBySiembra_IdSiembra(idSiembra).stream()
                .map(recomendacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listarPorAnomalia(Integer idAnomalia) {
        return recomendacionRepository.findByAnomalia_IdAnomalia(idAnomalia).stream()
                .map(recomendacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listarPorCategoria(String categoria) {
        return recomendacionRepository.findByCategoriaIgnoreCase(categoria).stream()
                .map(recomendacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listarPorPrioridad(PrioridadRecomendacion prioridad) {
        return recomendacionRepository.findByPrioridad(prioridad).stream()
                .map(recomendacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listarPorEstado(EstadoRecomendacion estado) {
        return recomendacionRepository.findByEstado(estado).stream()
                .map(recomendacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionResponseDTO> listarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta) {
        return recomendacionRepository.findByFechaGeneracionBetween(desde, hasta).stream()
                .map(recomendacionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RecomendacionResponseDTO buscarPorId(Integer idRecomendacion) {
        Recomendacion rec = recomendacionRepository.findById(idRecomendacion)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada con ID: " + idRecomendacion));
        return recomendacionMapper.toResponse(rec);
    }

    @Override
    public RecomendacionResponseDTO ignorar(Integer idRecomendacion) {
        Recomendacion rec = recomendacionRepository.findById(idRecomendacion)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada con ID: " + idRecomendacion));
        rec.setEstado(EstadoRecomendacion.IGNORADA);
        return recomendacionMapper.toResponse(recomendacionRepository.save(rec));
    }

    @Override
    public RecomendacionResponseDTO reaccionar(Integer idRecomendacion, String reaccion) {
        Recomendacion rec = recomendacionRepository.findById(idRecomendacion)
                .orElseThrow(() -> new RuntimeException("Recomendación no encontrada con ID: " + idRecomendacion));

        if (!reaccion.equalsIgnoreCase("UTIL") && !reaccion.equalsIgnoreCase("APLICADA")) {
            throw new RuntimeException("Reacción inválida. Use 'UTIL' o 'APLICADA'");
        }

        rec.setEstado(EstadoRecomendacion.valueOf(reaccion.toUpperCase()));
        return recomendacionMapper.toResponse(recomendacionRepository.save(rec));
    }

    @Override
    public void generarRecomendacionesClimaticas() {
        List<Siembra> siembras = siembraRepository.findAll().stream()
                .filter(s -> s.getFinca() != null && s.getFinca().getUbicacion() != null)
                .toList();

        Map<Integer, List<Siembra>> siembrasPorUbicacion = siembras.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getFinca().getUbicacion().getIdUbicacion()
                ));

        for (Map.Entry<Integer, List<Siembra>> entry : siembrasPorUbicacion.entrySet()) {
            Integer idUbicacion = entry.getKey();
            List<Siembra> siembrasPorUbic = entry.getValue();

            try {
                Clima clima = climaRepository
                        .findTopByUbicacion_IdUbicacionAndFechaMedicionOrderByFechaRegistroDesc(
                                idUbicacion, LocalDate.now())
                        .orElse(null);

                if (clima == null) continue;

                List<String> nombresCultivos = siembrasPorUbic.stream()
                        .map(s -> s.getCultivo().getNombre())
                        .distinct()
                        .toList();

                String prompt = promptBuilder.construirPromptClimaGlobal(clima, nombresCultivos);
                String respuesta = geminiService.generarRecomendacion(prompt);

                Map<String, String> recomendacionesPorCultivo = parsearRespuestaGemini(respuesta);

                for (Siembra siembra : siembrasPorUbic) {
                    String nombreCultivo = siembra.getCultivo().getNombre();
                    String descripcion = recomendacionesPorCultivo.getOrDefault(
                            nombreCultivo.toUpperCase(),
                            "Monitorear el cultivo según las condiciones climáticas actuales.");

                    Recomendacion rec = new Recomendacion();
                    rec.setDescripcion(descripcion);
                    rec.setCategoria("CLIMA");
                    rec.setPrioridad(PrioridadRecomendacion.MEDIA);
                    rec.setEstado(EstadoRecomendacion.PENDIENTE);
                    rec.setFechaGeneracion(LocalDateTime.now());
                    rec.setSiembra(siembra);
                    rec.setClima(clima);
                    rec.setAnomalia(null);
                    recomendacionRepository.save(rec);
                }

            } catch (Exception e) {
                System.err.println("Error generando recomendaciones climáticas para ubicación "
                        + idUbicacion + ": " + e.getMessage());
            }
        }
    }

    private Map<String, String> parsearRespuestaGemini(String respuesta) {
        Map<String, String> resultado = new HashMap<>();
        if (respuesta == null || respuesta.isBlank()) return resultado;

        for (String linea : respuesta.split("\n")) {
            if (linea.contains("CULTIVO:") && linea.contains("RECOMENDACION:")) {
                String[] partes = linea.split("\\|");
                if (partes.length == 2) {
                    String cultivo = partes[0].replace("CULTIVO:", "").trim().toUpperCase();
                    String recomendacion = partes[1].replace("RECOMENDACION:", "").trim();
                    resultado.put(cultivo, recomendacion);
                }
            }
        }
        return resultado;
    }

    private String obtenerEstadoActualCultivo(Integer idSiembra) {
        return siembraEstadoCultivoRepository.findPrimerEstado(idSiembra)
                .stream()
                .findFirst()
                .map(sec -> sec.getEstadoCultivo().getNombre())
                .orElse("desconocido");
    }

    private List<String> obtenerAnomaliasActivas(Integer idSiembra) {
        return anomaliaRepository.findBySiembraAndEstado(idSiembra, EstadoAnomalia.ACTIVA)
                .stream()
                .map(a -> a.getTipo().name().toLowerCase())
                .distinct()
                .toList();
    }

    private PrioridadRecomendacion determinarPrioridadManual(boolean tieneAnomaliasActivas,
                                                             boolean tieneAlertaClimatica) {
        if (tieneAnomaliasActivas || tieneAlertaClimatica) return PrioridadRecomendacion.ALTA;
        return PrioridadRecomendacion.MEDIA;
    }
}