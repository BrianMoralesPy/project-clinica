package com.clinica.module.medico.repository;

import com.clinica.module.medico.entity.Medico;
import com.clinica.shared.EstadoMedico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    boolean existsByDni(String dni);

    boolean existsByMatricula(String matricula);

    @Query("SELECT m FROM Medico m WHERE " +
           "(:especialidadId IS NULL OR m.especialidad.id = :especialidadId) AND " +
           "(:estado IS NULL OR m.estado = :estado)")
    Page<Medico> findByFilters(
        @Param("especialidadId") Long especialidadId,
        @Param("estado") EstadoMedico estado,
        Pageable pageable
    );
}
