package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.Entity.enums.EstadoTarea;
import com.agrotech.dto.request.ActualizarEstadoTareaRequestDTO;
import com.agrotech.dto.request.AsignarTareaRequestDTO;
import com.agrotech.dto.request.TareaRequestDTO;
import com.agrotech.dto.response.EjecucionTareaResponseDTO;
import com.agrotech.dto.response.TareaResponseDTO;
import com.agrotech.mapper.EjecucionTareaMapper;
import com.agrotech.mapper.TareaMapper;
import com.agrotech.repository.*;
import com.agrotech.service.TareaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
public class TareaServiceImpl implements TareaService {

    private final TareaRepository tareaRepository;
    private final EjecucionTareaRepository ejecucionTareaRepository;
    private final SiembraRepository siembraRepository;
    private final UsuarioRepository usuarioRepository;
    private final OperarioRepository operarioRepository;
    private final AuxiliarRepository auxiliarRepository;
    private final TipoTareaRepository tipoTareaRepository;
    private final TareaMapper tareaMapper;
    private final EjecucionTareaMapper ejecucionTareaMapper;

    public TareaServiceImpl(TareaRepository tareaRepository,
                            EjecucionTareaRepository ejecucionTareaRepository,
                            SiembraRepository siembraRepository,
                            UsuarioRepository usuarioRepository,
                            OperarioRepository operarioRepository,
                            AuxiliarRepository auxiliarRepository,
                            TipoTareaRepository tipoTareaRepository,
                            TareaMapper tareaMapper,
                            EjecucionTareaMapper ejecucionTareaMapper) {
        this.tareaRepository = tareaRepository;
        this.ejecucionTareaRepository = ejecucionTareaRepository;
        this.siembraRepository = siembraRepository;
        this.usuarioRepository = usuarioRepository;
        this.operarioRepository = operarioRepository;
        this.auxiliarRepository = auxiliarRepository;
        this.tipoTareaRepository = tipoTareaRepository;
        this.tareaMapper = tareaMapper;
        this.ejecucionTareaMapper = ejecucionTareaMapper;
    }


    @Override
    public TareaResponseDTO crear(TareaRequestDTO dto, String correoCreador) {
        usuarioRepository.findByCorreo(correoCreador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TipoTarea tipoTarea = tipoTareaRepository.findById(dto.getIdTipoTarea())
                .orElseThrow(() -> new RuntimeException("Tipo de tarea no encontrado: " + dto.getIdTipoTarea()));

        Siembra siembra = siembraRepository.findById(dto.getIdSiembra())
                .orElseThrow(() -> new RuntimeException("Siembra no encontrada: " + dto.getIdSiembra()));

        Tarea tarea = new Tarea();
        tarea.setTipoTarea(tipoTarea);
        tarea.setDescripcion(dto.getDescripcion());
        tarea.setFechaLimite(dto.getFechaLimite());
        tarea.setSiembra(siembra);

        return buildResponse(tareaRepository.save(tarea));
    }

    @Override
    public TareaResponseDTO asignar(Integer idTarea, AsignarTareaRequestDTO dto, String correoAsignador) {
        Usuario asignador = usuarioRepository.findByCorreo(correoAsignador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rolAsignador = asignador.getRol().getNombre();

        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + idTarea));

        if (dto.getIdOperario() == null && dto.getIdAuxiliar() == null) {
            throw new RuntimeException("Debe especificar un operario o auxiliar a asignar");
        }

        if ("OPERARIO".equals(rolAsignador) && dto.getIdOperario() != null) {
            throw new RuntimeException("El Operario solo puede asignar tareas a Auxiliares");
        }

        Integer idProductorAsignador = obtenerIdProductor(asignador, rolAsignador);

        List<EjecucionTarea> ejecucionesActuales = ejecucionTareaRepository
                .findByTarea_IdTarea(idTarea);

        if (dto.getIdOperario() != null) {
            boolean yaAsignado = ejecucionesActuales.stream()
                    .anyMatch(e -> e.getOperario() != null &&
                            e.getOperario().getIdUsuario().equals(dto.getIdOperario()));
            if (yaAsignado) {
                throw new RuntimeException("Este operario ya está asignado a la tarea");
            }
        }

        if (dto.getIdAuxiliar() != null) {
            boolean yaAsignado = ejecucionesActuales.stream()
                    .anyMatch(e -> e.getAuxiliar() != null &&
                            e.getAuxiliar().getIdUsuario().equals(dto.getIdAuxiliar()));
            if (yaAsignado) {
                throw new RuntimeException("Este auxiliar ya está asignado a la tarea");
            }
        }

        EjecucionTarea ejecucion = new EjecucionTarea();
        ejecucion.setTarea(tarea);
        ejecucion.setCreadoPor(asignador);
        ejecucion.setEstado(EstadoTarea.PENDIENTE);
        ejecucion.setFechaEstado(LocalDateTime.now());
        ejecucion.setFechaLimite(tarea.getFechaLimite());

        if (dto.getIdOperario() != null) {
            Operario operario = operarioRepository.findById(dto.getIdOperario())
                    .orElseThrow(() -> new RuntimeException("Operario no encontrado: " + dto.getIdOperario()));
            if (operario.getProductor() == null ||
                    !operario.getProductor().getIdUsuario().equals(idProductorAsignador)) {
                throw new RuntimeException("El Operario no pertenece a tu equipo");
            }
            ejecucion.setOperario(operario);
        }

        if (dto.getIdAuxiliar() != null) {
            Auxiliar auxiliar = auxiliarRepository.findById(dto.getIdAuxiliar())
                    .orElseThrow(() -> new RuntimeException("Auxiliar no encontrado: " + dto.getIdAuxiliar()));
            if (auxiliar.getProductor() == null ||
                    !auxiliar.getProductor().getIdUsuario().equals(idProductorAsignador)) {
                throw new RuntimeException("El Auxiliar no pertenece a tu equipo");
            }
            ejecucion.setAuxiliar(auxiliar);
        }

        ejecucionTareaRepository.save(ejecucion);
        return buildResponse(tarea);
    }

    @Override
    public EjecucionTareaResponseDTO actualizarEstado(Integer idEjecucion, ActualizarEstadoTareaRequestDTO dto, String correoSolicitante) {
        Usuario solicitante = usuarioRepository.findByCorreo(correoSolicitante)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        EjecucionTarea ejecucion = ejecucionTareaRepository.findById(idEjecucion)
                .orElseThrow(() -> new RuntimeException("Ejecución no encontrada: " + idEjecucion));

        boolean esAsignado = false;
        if (ejecucion.getOperario() != null &&
                ejecucion.getOperario().getIdUsuario().equals(solicitante.getIdUsuario())) {
            esAsignado = true;
        }
        if (ejecucion.getAuxiliar() != null &&
                ejecucion.getAuxiliar().getIdUsuario().equals(solicitante.getIdUsuario())) {
            esAsignado = true;
        }
        if ("PRODUCTOR".equals(solicitante.getRol().getNombre())) {
            esAsignado = true;
        }

        if (!esAsignado) {
            throw new RuntimeException("Solo el asignado puede actualizar el estado de esta tarea");
        }

        ejecucion.setEstado(dto.getEstado());
        ejecucion.setFechaEstado(LocalDateTime.now());
        ejecucionTareaRepository.save(ejecucion);

        return buildEjecucionResponse(ejecucion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaResponseDTO> listar(String correoSolicitante) {
        Usuario usuario = usuarioRepository.findByCorreo(correoSolicitante)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rol = usuario.getRol().getNombre();
        Integer idUsuario = usuario.getIdUsuario();

        return switch (rol) {
            case "PRODUCTOR" -> tareaRepository.findByProductor(idUsuario)
                    .stream()
                    .map(this::buildResponse)
                    .toList();

            case "OPERARIO" -> {
                List<Tarea> tareasAsignadas = ejecucionTareaRepository
                        .findByOperario_IdUsuario(idUsuario)
                        .stream()
                        .map(EjecucionTarea::getTarea)
                        .toList();

                List<Tarea> tareasCreadas = ejecucionTareaRepository
                        .findByCreadoPor_IdUsuario(idUsuario)
                        .stream()
                        .map(EjecucionTarea::getTarea)
                        .toList();

                yield Stream.concat(tareasAsignadas.stream(), tareasCreadas.stream())
                        .distinct()
                        .map(t -> buildResponseParaOperario(t, idUsuario))
                        .toList();
            }

            case "AUXILIAR" ->
                    ejecucionTareaRepository.findByAuxiliar_IdUsuario(idUsuario)
                            .stream()
                            .map(e -> buildResponseParaAuxiliar(e.getTarea(), idUsuario))
                            .distinct()
                            .toList();

            default -> List.of();
        };
    }

    @Override
    @Transactional(readOnly = true)
    public TareaResponseDTO buscarPorId(Integer idTarea, String correoSolicitante) {
        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada: " + idTarea));
        return buildResponse(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TareaResponseDTO> listarPorSiembra(Integer idSiembra, String correoSolicitante) {
        return tareaRepository.findBySiembra_IdSiembra(idSiembra)
                .stream().map(this::buildResponse).toList();
    }


    private Integer obtenerIdProductor(Usuario usuario, String rol) {
        return switch (rol) {
            case "PRODUCTOR" -> usuario.getIdUsuario();
            case "OPERARIO" -> {
                Operario operario = operarioRepository.findById(usuario.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Operario no encontrado"));
                if (operario.getProductor() == null) {
                    throw new RuntimeException("El Operario no tiene Productor asignado");
                }
                yield operario.getProductor().getIdUsuario();
            }
            default -> throw new RuntimeException("Rol no permitido para asignar tareas");
        };
    }

    private TareaResponseDTO buildResponse(Tarea tarea) {
        TareaResponseDTO response = tareaMapper.toResponse(tarea);

        response.setNombreSiembra(tarea.getSiembra().getCultivo().getNombre()
                + " - Lote " + tarea.getSiembra().getNumLote());

        List<EjecucionTareaResponseDTO> asignaciones = ejecucionTareaRepository
                .findByTarea_IdTarea(tarea.getIdTarea())
                .stream()
                .map(this::buildEjecucionResponse)
                .toList();
        response.setAsignaciones(asignaciones);
        return response;
    }

    private EjecucionTareaResponseDTO buildEjecucionResponse(EjecucionTarea e) {
        EjecucionTareaResponseDTO dto = ejecucionTareaMapper.toResponse(e);
        if (e.getCreadoPor() != null) {
            dto.setCreadoPor(e.getCreadoPor().getNombre() + " " + e.getCreadoPor().getApellido());
        }

        if (e.getOperario() != null) {
            dto.setOperarioAsignado(e.getOperario().getNombre() + " " + e.getOperario().getApellido());
        }

        if (e.getAuxiliar() != null) {
            dto.setAuxiliarAsignado(e.getAuxiliar().getNombre() + " " + e.getAuxiliar().getApellido());
        }
        return dto;
    }

    private TareaResponseDTO buildResponseParaOperario(Tarea tarea, Integer idOperario) {
        TareaResponseDTO response = tareaMapper.toResponse(tarea);
        response.setNombreSiembra(tarea.getSiembra().getCultivo().getNombre()
                + " - Lote " + tarea.getSiembra().getNumLote());

        List<EjecucionTareaResponseDTO> asignaciones = ejecucionTareaRepository
                .findByTarea_IdTarea(tarea.getIdTarea())
                .stream()
                .filter(e ->
                        (e.getOperario() != null && e.getOperario().getIdUsuario().equals(idOperario)) ||
                                (e.getCreadoPor() != null && e.getCreadoPor().getIdUsuario().equals(idOperario))
                )
                .map(this::buildEjecucionResponse)
                .toList();
        response.setAsignaciones(asignaciones);
        return response;
    }

    private TareaResponseDTO buildResponseParaAuxiliar(Tarea tarea, Integer idAuxiliar) {
        TareaResponseDTO response = tareaMapper.toResponse(tarea);
        response.setNombreSiembra(tarea.getSiembra().getCultivo().getNombre()
                + " - Lote " + tarea.getSiembra().getNumLote());

        List<EjecucionTareaResponseDTO> asignaciones = ejecucionTareaRepository
                .findByTarea_IdTarea(tarea.getIdTarea())
                .stream()
                .filter(e -> e.getAuxiliar() != null &&
                        e.getAuxiliar().getIdUsuario().equals(idAuxiliar))
                .map(this::buildEjecucionResponse)
                .toList();
        response.setAsignaciones(asignaciones);
        return response;
    }
}
