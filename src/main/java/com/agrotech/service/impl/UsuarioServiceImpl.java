package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.dto.request.EditarPerfilRequestDTO;
import com.agrotech.dto.request.UsuarioRequestDTO;
import com.agrotech.dto.request.UsuarioUpdateRequestDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;
import com.agrotech.mapper.UsuarioMapper;
import com.agrotech.repository.*;
import com.agrotech.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private static final Set<String> ROLES_PERMITIDOS_PRODUCTOR = Set.of("OPERARIO", "AUXILIAR");

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final ProductorRepository productorRepository;
    private final OperarioRepository operarioRepository;
    private final AuxiliarRepository auxiliarRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              RolRepository rolRepository,
                              PasswordEncoder passwordEncoder,
                              UsuarioMapper usuarioMapper,
                              ProductorRepository productorRepository,
                              OperarioRepository operarioRepository,
                              AuxiliarRepository auxiliarRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.productorRepository = productorRepository;
        this.operarioRepository = operarioRepository;
        this.auxiliarRepository = auxiliarRepository;
    }

    @Override
    public UsuarioResponseDTO crear(UsuarioRequestDTO dto, String correoCreador) {

        Usuario creador = usuarioRepository.findByCorreo(correoCreador).orElseThrow(() -> new RuntimeException("Creador no encontrado"));

        String rolCreador = creador.getRol().getNombre();
        String rolNuevo = dto.getRol().toUpperCase();

        if ("PRODUCTOR".equals(rolCreador) && !ROLES_PERMITIDOS_PRODUCTOR.contains(rolNuevo)) {
            throw new RuntimeException("El Productor solo puede crear usuarios con rol OPERARIO o AUXILIAR");
        }

        // Validar correo único
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + dto.getCorreo());
        }

        Rol rol = rolRepository.findByNombre(rolNuevo).orElseThrow(() -> new RuntimeException("El rol no encontrado: " + rolNuevo));

        Usuario usuario = crearEntidadSegunRol(rolNuevo);
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setCorreo(dto.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setRol(rol);
        usuario.setActivo(true);

        if ("PRODUCTOR".equals(rolCreador)) {
            Productor productor = productorRepository.findById(creador.getIdUsuario()).orElseThrow(() -> new RuntimeException("Productor no encontrado"));

            if (usuario instanceof Operario operario) {
                operario.setProductor(productor);
            } else if (usuario instanceof Auxiliar auxiliar) {
                auxiliar.setProductor(productor);
            }
        }

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar(String correoSolicitante) {
        Usuario solicitante = usuarioRepository.findByCorreo(correoSolicitante).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if ("ADMINISTRADOR".equals(solicitante.getRol().getNombre())) {
            return usuarioRepository.findAll().stream()
                    .map(usuarioMapper::toResponse)
                    .toList();
        }

        // PRODUCTOR: solo sus operarios y auxiliares
        Integer idProductor = solicitante.getIdUsuario();
        List<Usuario> suyos = new java.util.ArrayList<>();
        suyos.addAll(operarioRepository.findByProductor_IdUsuario(idProductor));
        suyos.addAll(auxiliarRepository.findByProductor_IdUsuario(idProductor));
        return suyos.stream().map(usuarioMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarPorNombre(String nombre, String correoSolicitante) {
        return listar(correoSolicitante).stream()
                .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarPorCorreo(String correo, String correoSolicitante) {
        return listar(correoSolicitante).stream()
                .filter(u -> u.getCorreo().toLowerCase().contains(correo.toLowerCase()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> buscarPorRol(String nombreRol, String correoSolicitante) {
        return listar(correoSolicitante).stream()
                .filter(u -> u.getRol().equalsIgnoreCase(nombreRol))
                .toList();
    }

    @Override
    public UsuarioResponseDTO actualizar(Integer idUsuario, UsuarioUpdateRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());
        if (dto.getFechaNacimiento() != null) usuario.setFechaNacimiento(dto.getFechaNacimiento());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        if (dto.getRol() != null && !dto.getRol().isBlank()) {
            Rol rol = rolRepository.findByNombre(dto.getRol().toUpperCase())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + dto.getRol()));
            usuario.setRol(rol);
        }

        if (dto.getCorreo() != null && !dto.getCorreo().equals(usuario.getCorreo())) {
            if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
                throw new RuntimeException("El correo ya está en uso: " + dto.getCorreo());
            }
            usuario.setCorreo(dto.getCorreo());
        }

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Integer idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        usuarioRepository.deleteById(idUsuario);
    }

    @Override
    public UsuarioResponseDTO desactivar(Integer idUsuario, String correoSolicitante) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idUsuario));

        String rol = usuario.getRol().getNombre();
        if (!"OPERARIO".equals(rol) && !"AUXILIAR".equals(rol)) {
            throw new RuntimeException("Solo se pueden desactivar usuarios con rol OPERARIO o AUXILIAR");
        }

        Usuario solicitante = usuarioRepository.findByCorreo(correoSolicitante).orElseThrow(() -> new RuntimeException("Solicitante no encontrado"));

        if ("PRODUCTOR".equals(solicitante.getRol().getNombre())) {
            Integer idProductor = solicitante.getIdUsuario();

            boolean esSuyo = false;
            if (usuario instanceof Operario operario) {
                esSuyo = operario.getProductor() != null &&
                        operario.getProductor().getIdUsuario().equals(idProductor);
            } else if (usuario instanceof Auxiliar auxiliar) {
                esSuyo = auxiliar.getProductor() != null &&
                        auxiliar.getProductor().getIdUsuario().equals(idProductor);
            }

            if (!esSuyo) {
                throw new RuntimeException("No puedes desactivar usuarios que no creaste");
            }
        }

        // Desenlazar del productor
        if (usuario instanceof Operario operario) {
            operario.setProductor(null);
        } else if (usuario instanceof Auxiliar auxiliar) {
            auxiliar.setProductor(null);
        }

        usuario.setActivo(false);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO verPerfil(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponseDTO editarPerfil(String correo, EditarPerfilRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto.getApellido() != null) usuario.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) usuario.setTelefono(dto.getTelefono());
        if (dto.getFechaNacimiento() != null) usuario.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }
        if (dto.getCorreo() != null && !dto.getCorreo().equals(correo)) {
            if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
                throw new RuntimeException("El correo ya está en uso: " + dto.getCorreo());
            }
            usuario.setCorreo(dto.getCorreo());
        }

        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminarPerfil(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private Usuario crearEntidadSegunRol(String rol) {
        return switch (rol) {
            case "PRODUCTOR"     -> new Productor();
            case "OPERARIO"      -> new Operario();
            case "AUXILIAR"      -> new Auxiliar();
            default              -> new Usuario(); // ADMINISTRADOR u otros
        };
    }

}