package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.dto.request.SiembraRequestDTO;
import com.agrotech.dto.request.SiembraUpdateRequestDTO;
import com.agrotech.dto.response.SiembraResponseDTO;
import com.agrotech.mapper.SiembraMapper;
import com.agrotech.repository.*;
import com.agrotech.service.SiembraService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SiembraServiceImpl implements SiembraService {

    private final SiembraRepository siembraRepository;
    private final FincaRepository fincaRepository;
    private final CultivoRepository cultivoRepository;
    private final SiembraMapper siembraMapper;
    private final EstadoCultivoRepository estadoCultivoRepository;
    private final SiembraEstadoCultivoRepository siembraEstadoCultivoRepository;
    private final UsuarioRepository usuarioRepository;
    private final OperarioRepository operarioRepository;
    private final AuxiliarRepository auxiliarRepository;

    public SiembraServiceImpl(SiembraRepository siembraRepository, FincaRepository fincaRepository,
                              CultivoRepository cultivoRepository, SiembraMapper siembraMapper, EstadoCultivoRepository estadoCultivoRepository,
                              SiembraEstadoCultivoRepository siembraEstadoCultivoRepository, UsuarioRepository usuarioRepository, OperarioRepository operarioRepository,
                              AuxiliarRepository auxiliarRepository) {
        this.siembraRepository = siembraRepository;
        this.fincaRepository = fincaRepository;
        this.cultivoRepository = cultivoRepository;
        this.siembraMapper = siembraMapper;
        this.estadoCultivoRepository = estadoCultivoRepository;
        this.siembraEstadoCultivoRepository = siembraEstadoCultivoRepository;
        this.usuarioRepository = usuarioRepository;
        this.operarioRepository = operarioRepository;
        this.auxiliarRepository = auxiliarRepository;
    }

    @Override
    public SiembraResponseDTO crear(SiembraRequestDTO dto, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);

        Finca finca = fincaRepository.findById(dto.getIdFinca())
                .orElseThrow(() -> new RuntimeException("Finca no encontrada: " + dto.getIdFinca()));

        if (!idFincas.contains(finca.getIdFinca())) {
            throw new RuntimeException("No tienes acceso a esta finca");
        }

        Cultivo cultivo = cultivoRepository.findById(dto.getIdCultivo())
                .orElseThrow(() -> new RuntimeException("Cultivo no encontrado: " + dto.getIdCultivo()));

        EstadoCultivo estado = estadoCultivoRepository.findById(dto.getIdEstadoCultivo())
                .orElseThrow(() -> new RuntimeException("EstadoCultivo no encontrado: " + dto.getIdEstadoCultivo()));

        if (dto.getNumLote() < 1 || dto.getNumLote() > finca.getNumLotes()) {
            throw new RuntimeException("Número de lote inválido. Debe estar entre 1 y " + finca.getNumLotes());
        }

        Siembra siembra = new Siembra();
        siembra.setFinca(finca);
        siembra.setCultivo(cultivo);
        siembra.setNumLote(dto.getNumLote());
        siembraRepository.save(siembra);

        SiembraEstadoCultivo estadoInicial = new SiembraEstadoCultivo(
                siembra, estado, dto.getFechaEstado()
        );
        siembraEstadoCultivoRepository.save(estadoInicial);

        SiembraResponseDTO response = siembraMapper.toResponse(siembra);
        response.setNombreEstado(estado.getNombre());
        response.setFechaEstado(dto.getFechaEstado());
        response.setFechaSiembra(dto.getFechaEstado());
        return response;
    }

    @Override
    public List<SiembraResponseDTO> listar(String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        if (idFincas.isEmpty()) return List.of();
        return idFincas.stream()
                .flatMap(idFinca -> siembraRepository.findByFincaConUltimoEstado(idFinca).stream())
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public List<SiembraResponseDTO> buscarPorFinca(Integer idFinca, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        if (!idFincas.contains(idFinca)) {
            throw new RuntimeException("No tienes acceso a esta finca");
        }
        return siembraRepository.findByFincaConUltimoEstado(idFinca).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public List<SiembraResponseDTO> buscarPorCultivo(Integer idCultivo, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        return siembraRepository.findByCultivoConUltimoEstado(idCultivo).stream()
                .filter(s -> idFincas.contains(s.getFinca().getIdFinca()))
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public List<SiembraResponseDTO> buscarPorFincaYCultivo(Integer idFinca, Integer idCultivo, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        if (!idFincas.contains(idFinca)) {
            throw new RuntimeException("No tienes acceso a esta finca");
        }
        return siembraRepository.findByFincaCultivoYUltimoEstado(idFinca, idCultivo).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public List<SiembraResponseDTO> buscarPorFincaYLote(Integer idFinca, Integer numLote, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        if (!idFincas.contains(idFinca)) {
            throw new RuntimeException("No tienes acceso a esta finca");
        }
        return siembraRepository.findByFincaAndNumLoteConUltimoEstado(idFinca, numLote).stream()
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public List<SiembraResponseDTO> buscarPorEstado(Integer idEstado, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        return siembraRepository.findByEstadoCultivoConUltimoEstado(idEstado).stream()
                .filter(s -> idFincas.contains(s.getFinca().getIdFinca()))
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public List<SiembraResponseDTO> buscarPorRangoFechas(LocalDateTime desde, LocalDateTime hasta, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        return siembraRepository.findByRangoFechaConUltimoEstado(desde, hasta).stream()
                .filter(s -> idFincas.contains(s.getFinca().getIdFinca()))
                .map(this::buildResponse)
                .toList();
    }

    @Override
    public SiembraResponseDTO actualizar(Integer idSiembra, SiembraUpdateRequestDTO dto, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        Siembra siembra = siembraRepository.findById(idSiembra)
                .orElseThrow(() -> new RuntimeException("Siembra no encontrada con ID: " + idSiembra));

        if (!idFincas.contains(siembra.getFinca().getIdFinca())) {
            throw new RuntimeException("No tienes acceso a esta siembra");
        }

        if (dto.getNumLote() != null) {
            Finca finca = dto.getIdFinca() != null
                    ? fincaRepository.findById(dto.getIdFinca())
                    .orElseThrow(() -> new RuntimeException("Finca no encontrada"))
                    : siembra.getFinca();

            if (dto.getNumLote() < 1 || dto.getNumLote() > finca.getNumLotes()) {
                throw new RuntimeException("Número de lote inválido. Debe estar entre 1 y " + finca.getNumLotes());
            }
            siembra.setNumLote(dto.getNumLote());
            siembra.setFinca(finca);
        }

        if (dto.getIdCultivo() != null) {
            Cultivo cultivo = cultivoRepository.findById(dto.getIdCultivo())
                    .orElseThrow(() -> new RuntimeException("Cultivo no encontrado"));
            siembra.setCultivo(cultivo);
        }

        siembraRepository.save(siembra);

        return buildResponse(siembra);
    }

    @Override
    public void eliminar(Integer idSiembra, String correo) {
        List<Integer> idFincas = obtenerIdFincasAccesibles(correo);
        Siembra siembra = siembraRepository.findById(idSiembra)
                .orElseThrow(() -> new RuntimeException("Siembra no encontrada: " + idSiembra));

        if (!idFincas.contains(siembra.getFinca().getIdFinca())) {
            throw new RuntimeException("No tienes acceso a esta siembra");
        }

        siembraRepository.deleteById(idSiembra);
    }

    private SiembraResponseDTO buildResponse(Siembra siembra) {
        SiembraResponseDTO response = siembraMapper.toResponse(siembra);

        // Último estado (el más reciente)
        siembraEstadoCultivoRepository.findUltimoEstado(siembra.getIdSiembra())
                .stream()
                .findFirst()
                .ifPresent(sec -> {
                    response.setNombreEstado(sec.getEstadoCultivo().getNombre());
                    response.setFechaEstado(sec.getFechaEstado());
                });

        // Primer estado (fecha de siembra original)
        siembraEstadoCultivoRepository.findPrimerEstado(siembra.getIdSiembra())
                .stream()
                .findFirst()
                .ifPresent(sec -> response.setFechaSiembra(sec.getFechaEstado()));

        return response;
    }

    private List<Integer> obtenerIdFincasAccesibles(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String rol = usuario.getRol().getNombre();

        if ("PRODUCTOR".equals(rol)) {
            return fincaRepository.findByProductor_IdUsuario(usuario.getIdUsuario())
                    .stream()
                    .map(Finca::getIdFinca)
                    .toList();
        }

        if ("OPERARIO".equals(rol)) {
            Operario operario = operarioRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Operario no encontrado"));
            if (operario.getProductor() == null) return List.of();
            return fincaRepository.findByProductor_IdUsuario(operario.getProductor().getIdUsuario())
                    .stream()
                    .map(Finca::getIdFinca)
                    .toList();
        }

        if ("AUXILIAR".equals(rol)) {
            Auxiliar auxiliar = auxiliarRepository.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Auxiliar no encontrado"));
            if (auxiliar.getProductor() == null) return List.of();
            return fincaRepository.findByProductor_IdUsuario(auxiliar.getProductor().getIdUsuario())
                    .stream()
                    .map(Finca::getIdFinca)
                    .toList();
        }

        return List.of();
    }
}
