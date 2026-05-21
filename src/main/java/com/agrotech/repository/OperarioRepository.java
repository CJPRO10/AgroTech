package com.agrotech.repository;

import com.agrotech.Entity.Operario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperarioRepository extends JpaRepository<Operario, Integer> {
    List<Operario> findByProductor_IdUsuario(Integer idProductor);
}
