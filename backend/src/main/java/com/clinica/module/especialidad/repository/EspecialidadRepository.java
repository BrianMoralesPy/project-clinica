package com.clinica.module.especialidad.repository;

import com.clinica.module.especialidad.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    boolean existsByNombre(String nombre);

    Optional<Especialidad> findByNombre(String nombre);
}
