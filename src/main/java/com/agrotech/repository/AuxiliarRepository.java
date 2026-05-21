package com.agrotech.repository;

import com.agrotech.Entity.Auxiliar;
import com.agrotech.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AuxiliarRepository extends JpaRepository<Auxiliar, Integer> {
    List<Auxiliar> findByProductor_IdUsuario(Integer idProductor);
}
