package com.agrotech.repository;

import com.agrotech.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Usuario> findByNombreContainingIgnoreCase(String nombre);

    List<Usuario> findByRol_Nombre(String nombreRol);

    List<Usuario> findByCorreoContainingIgnoreCase(String correo);
}
