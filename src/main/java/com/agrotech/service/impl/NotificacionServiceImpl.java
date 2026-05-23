package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.Entity.enums.*;
import com.agrotech.dto.request.PreferenciaNotificacionRequestDTO;
import com.agrotech.dto.response.NotificacionResponseDTO;
import com.agrotech.mapper.NotificacionMapper;
import com.agrotech.repository.*;
import com.agrotech.service.NotificacionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final PreferenciaNotificacionRepository preferenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductorRepository productorRepository;
    private final OperarioRepository operarioRepository;
    private final AuxiliarRepository auxiliarRepository;
    private final NotificacionMapper notificacionMapper;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository,
                                   PreferenciaNotificacionRepository preferenciaRepository,
                                   UsuarioRepository usuarioRepository,
                                   ProductorRepository productorRepository,
                                   OperarioRepository operarioRepository,
                                   AuxiliarRepository auxiliarRepository,
                                   NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.preferenciaRepository = preferenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productorRepository = productorRepository;
        this.operarioRepository = operarioRepository;
        this.auxiliarRepository = auxiliarRepository;
        this.notificacionMapper = notificacionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> listar(String correo) {
        Usuario usuario = obtenerUsuario(correo);
        return notificacionRepository
                .findByUsuario_IdUsuarioOrderByFechaCreacionDesc(usuario.getIdUsuario())
                .stream()
                .map(n -> {
                    NotificacionResponseDTO dto = notificacionMapper.toResponse(n);
                    dto.setNombreUsuario(n.getUsuario().getNombre() + " " + n.getUsuario().getApellido());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> filtrarPorTipo(String correo, TipoNotificacion tipo) {
        Usuario usuario = obtenerUsuario(correo);
        return notificacionRepository
                .findByUsuario_IdUsuarioAndTipo(usuario.getIdUsuario(), tipo)
                .stream()
                .map(n -> {
                    NotificacionResponseDTO dto = notificacionMapper.toResponse(n);
                    dto.setNombreUsuario(n.getUsuario().getNombre() + " " + n.getUsuario().getApellido());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> filtrarPorPrioridad(String correo, PrioridadNotificacion prioridad) {
        Usuario usuario = obtenerUsuario(correo);
        return notificacionRepository
                .findByUsuario_IdUsuarioAndPrioridad(usuario.getIdUsuario(), prioridad)
                .stream()
                .map(n -> {
                    NotificacionResponseDTO dto = notificacionMapper.toResponse(n);
                    dto.setNombreUsuario(n.getUsuario().getNombre() + " " + n.getUsuario().getApellido());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> filtrarPorEstado(String correo, EstadoNotificacion estado) {
        Usuario usuario = obtenerUsuario(correo);
        return notificacionRepository
                .findByUsuario_IdUsuarioAndEstado(usuario.getIdUsuario(), estado)
                .stream()
                .map(n -> {
                    NotificacionResponseDTO dto = notificacionMapper.toResponse(n);
                    // Nombre completo en lugar de solo nombre
                    dto.setNombreUsuario(n.getUsuario().getNombre() + " " + n.getUsuario().getApellido());
                    return dto;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> filtrarPorFechas(String correo, LocalDateTime desde, LocalDateTime hasta) {
        Usuario usuario = obtenerUsuario(correo);
        return notificacionRepository
                .findByUsuario_IdUsuarioAndFechaCreacionBetween(usuario.getIdUsuario(), desde, hasta)
                .stream()
                .map(n -> {
                    NotificacionResponseDTO dto = notificacionMapper.toResponse(n);
                    dto.setNombreUsuario(n.getUsuario().getNombre() + " " + n.getUsuario().getApellido());
                    return dto;
                })
                .toList();
    }

    @Override
    public NotificacionResponseDTO marcarComoLeida(Integer idNotificacion, String correo) {
        Notificacion notificacion = notificacionRepository.findById(idNotificacion)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada: " + idNotificacion));

        Usuario usuario = obtenerUsuario(correo);
        if (!notificacion.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
            throw new RuntimeException("No tienes permiso para marcar esta notificación");
        }

        notificacion.setEstado(EstadoNotificacion.LEIDA);
        return notificacionMapper.toResponse(notificacionRepository.save(notificacion));
    }

    @Override
    public void marcarTodasComoLeidas(String correo) {
        Usuario usuario = obtenerUsuario(correo);
        notificacionRepository
                .findByUsuario_IdUsuarioAndEstado(usuario.getIdUsuario(), EstadoNotificacion.NO_LEIDA)
                .forEach(n -> {
                    n.setEstado(EstadoNotificacion.LEIDA);
                    notificacionRepository.save(n);
                });
    }

    @Override
    public void generarNotificacionAnomalia(Anomalia anomalia) {
        String titulo = "Nueva anomalía detectada: " + anomalia.getNombre();
        String mensaje = "Se registró una anomalía de tipo " + anomalia.getTipo().name()
                + " con severidad " + anomalia.getNivelSeveridad().name()
                + " en " + anomalia.getSiembra().getFinca().getNombreFinca()
                + " - Lote " + anomalia.getSiembra().getNumLote();

        PrioridadNotificacion prioridad = switch (anomalia.getNivelSeveridad()) {
            case CRITICA, ALTA -> PrioridadNotificacion.ALTA;
            case MEDIA         -> PrioridadNotificacion.MEDIA;
            case BAJA          -> PrioridadNotificacion.BAJA;
        };

        List<Usuario> destinatarios = obtenerDestinatariosDeFinca(
                anomalia.getSiembra().getFinca().getProductor());

        for (Usuario destinatario : destinatarios) {
            if (debeRecibirNotificacion(destinatario, TipoNotificacion.ANOMALIA, prioridad)) {
                Notificacion n = new Notificacion();
                n.setTitulo(titulo);
                n.setMensaje(mensaje);
                n.setTipo(TipoNotificacion.ANOMALIA);
                n.setPrioridad(prioridad);
                n.setEstado(EstadoNotificacion.NO_LEIDA);
                n.setFechaCreacion(LocalDateTime.now());
                n.setUsuario(destinatario);
                n.setAnomalia(anomalia);
                notificacionRepository.save(n);
            }
        }
    }

    @Override
    public void generarNotificacionRecomendacion(Recomendacion recomendacion) {
        String titulo = "Nueva recomendación generada";
        String mensaje = "Se generó una recomendación de categoría "
                + recomendacion.getCategoria()
                + " con prioridad " + recomendacion.getPrioridad().name()
                + " para " + recomendacion.getSiembra().getFinca().getNombreFinca();

        PrioridadNotificacion prioridad = switch (recomendacion.getPrioridad()) {
            case ALTA  -> PrioridadNotificacion.ALTA;
            case MEDIA -> PrioridadNotificacion.MEDIA;
            case BAJA  -> PrioridadNotificacion.BAJA;
        };

        List<Usuario> destinatarios = obtenerDestinatariosDeFinca(
                recomendacion.getSiembra().getFinca().getProductor());

        for (Usuario destinatario : destinatarios) {
            if (debeRecibirNotificacion(destinatario, TipoNotificacion.RECOMENDACION, prioridad)) {
                Notificacion n = new Notificacion();
                n.setTitulo(titulo);
                n.setMensaje(mensaje);
                n.setTipo(TipoNotificacion.RECOMENDACION);
                n.setPrioridad(prioridad);
                n.setEstado(EstadoNotificacion.NO_LEIDA);
                n.setFechaCreacion(LocalDateTime.now());
                n.setUsuario(destinatario);
                n.setRecomendacion(recomendacion);
                notificacionRepository.save(n);
            }
        }
    }

    @Override
    public void generarNotificacionClima(Clima clima, String mensaje) {
        List<Productor> productores = productorRepository.findAll().stream()
                .filter(p -> p.getFincas() != null && p.getFincas().stream()
                        .anyMatch(f -> f.getUbicacion() != null &&
                                f.getUbicacion().getIdUbicacion()
                                        .equals(clima.getUbicacion().getIdUbicacion())))
                .toList();

        for (Productor productor : productores) {
            List<Usuario> destinatarios = obtenerDestinatariosDeFinca(productor);
            for (Usuario destinatario : destinatarios) {
                if (debeRecibirNotificacion(destinatario, TipoNotificacion.CLIMA, PrioridadNotificacion.ALTA)) {
                    Notificacion n = new Notificacion();
                    n.setTitulo("Alerta climática");
                    n.setMensaje(mensaje);
                    n.setTipo(TipoNotificacion.CLIMA);
                    n.setPrioridad(PrioridadNotificacion.ALTA);
                    n.setEstado(EstadoNotificacion.NO_LEIDA);
                    n.setFechaCreacion(LocalDateTime.now());
                    n.setUsuario(destinatario);
                    n.setClima(clima);
                    notificacionRepository.save(n);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDTO> obtenerPreferencias(String correo) {
        return filtrarPorEstado(correo, EstadoNotificacion.NO_LEIDA);
    }

    @Override
    public void configurarPreferencia(String correo, PreferenciaNotificacionRequestDTO dto) {
        Usuario usuario = obtenerUsuario(correo);

        PreferenciaNotificacion preferencia = preferenciaRepository
                .findByUsuario_IdUsuarioAndTipoAlerta(usuario.getIdUsuario(), dto.getTipoAlerta())
                .orElseGet(() -> {
                    PreferenciaNotificacion nueva = new PreferenciaNotificacion();
                    nueva.setUsuario(usuario);
                    nueva.setTipoAlerta(dto.getTipoAlerta());
                    return nueva;
                });

        preferencia.setActivo(dto.isActivo());
        preferencia.setNivelMinimoPrioridad(dto.getNivelMinimoPrioridad());
        preferenciaRepository.save(preferencia);
    }

    private List<Usuario> obtenerDestinatariosDeFinca(Productor productor) {
        List<Usuario> destinatarios = new ArrayList<>();
        destinatarios.add(productor);
        destinatarios.addAll(operarioRepository.findByProductor_IdUsuario(productor.getIdUsuario()));
        destinatarios.addAll(auxiliarRepository.findByProductor_IdUsuario(productor.getIdUsuario()));
        return destinatarios;
    }

    private boolean debeRecibirNotificacion(Usuario usuario, TipoNotificacion tipo,
                                            PrioridadNotificacion prioridad) {
        return preferenciaRepository
                .findByUsuario_IdUsuarioAndTipoAlerta(usuario.getIdUsuario(), tipo)
                .map(p -> {
                    if (!p.isActivo()) return false;
                    if (p.getNivelMinimoPrioridad() == null) return true;
                    return prioridad.ordinal() <= p.getNivelMinimoPrioridad().ordinal();
                })
                .orElse(true);
    }

    private Usuario obtenerUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}