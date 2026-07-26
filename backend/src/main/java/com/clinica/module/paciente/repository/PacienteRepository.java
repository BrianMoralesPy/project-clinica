package com.clinica.module.paciente.repository;

import com.clinica.module.paciente.entity.Paciente;
import com.clinica.shared.EstadoPaciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByDni(String dni);

    @Query("SELECT p FROM Paciente p WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(p.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.dni) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:estado IS NULL OR p.estado = :estado)")
    Page<Paciente> findBySearchAndEstado(
        @Param("search") String search,
        @Param("estado") EstadoPaciente estado,
        Pageable pageable
    );
}
