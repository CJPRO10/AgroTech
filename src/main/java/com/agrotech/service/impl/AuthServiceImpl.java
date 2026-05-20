package com.agrotech.service.impl;

import com.agrotech.Entity.*;
import com.agrotech.dto.request.LoginRequestDTO;
import com.agrotech.dto.request.RegistroProductorRequestDTO;
import com.agrotech.dto.response.LoginResponseDTO;
import com.agrotech.dto.response.UsuarioResponseDTO;
import com.agrotech.mapper.UsuarioMapper;
import com.agrotech.repository.*;
import com.agrotech.security.JwtUtil;
import com.agrotech.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ProductorRepository productorRepository;
    private final FincaRepository fincaRepository;
    private final UbicacionRepository ubicacionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           ProductorRepository productorRepository,
                           FincaRepository fincaRepository,
                           UbicacionRepository ubicacionRepository,
                           PasswordEncoder passwordEncoder,
                           UsuarioMapper usuarioMapper,
                           JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.productorRepository = productorRepository;
        this.fincaRepository = fincaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        // Valida credenciales, lanza excepción si son incorrectas
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getCorreo(),
                        dto.getContrasena()
                )
        );

        Usuario usuario = usuarioRepository.findByCorreo(dto.getCorreo()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(
                usuario.getCorreo(),
                usuario.getRol().getNombre()
        );

        return new LoginResponseDTO(
                token,
                usuario.getCorreo(),
                usuario.getNombre() + " " + usuario.getApellido(),
                usuario.getRol().getNombre()
        );
    }

    @Override
    public UsuarioResponseDTO registrarProductor(RegistroProductorRequestDTO dto) {
        // Validar correo único
        if (usuarioRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("Ya existe un usuario con el correo: " + dto.getCorreo());
        }

        // Validar hectáreas <= 5
        if (dto.getHectareas() == null || dto.getHectareas() > 5.0) {
            throw new RuntimeException("Las hectáreas no pueden exceder 5");
        }

        // Obtener rol PRODUCTOR
        Rol rolProductor = rolRepository.findByNombre("PRODUCTOR").orElseThrow(() -> new RuntimeException("Rol PRODUCTOR no encontrado en BD"));

        // Obtener ubicación
        Ubicacion ubicacion = ubicacionRepository.findById(dto.getIdUbicacion()).orElseThrow(() -> new RuntimeException("Ubicación no encontrada: " + dto.getIdUbicacion()));

        // Crear Productor
        Productor productor = new Productor();
        productor.setNombre(dto.getNombre());
        productor.setApellido(dto.getApellido());
        productor.setCorreo(dto.getCorreo());
        productor.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        productor.setTelefono(dto.getTelefono());
        productor.setFechaNacimiento(dto.getFechaNacimiento());
        productor.setActivo(true);
        productor.setRol(rolProductor);

        productor = productorRepository.save(productor);

        // Crear Finca asociada al productor
        Finca finca = new Finca();
        finca.setNombreFinca(dto.getNombreFinca());
        finca.setHectareas(BigDecimal.valueOf(dto.getHectareas()));
        finca.setNumLotes(dto.getNumLotes());
        finca.setUbicacion(ubicacion);
        finca.setProductor(productor);

        fincaRepository.save(finca);

        return usuarioMapper.toResponse(productor);
    }

}