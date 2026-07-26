package com.clinica.module.turno.repository;

import com.clinica.module.turno.entity.Turno;
import com.clinica.shared.EstadoTurno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    @Query("SELECT t FROM Turno t WHERE " +
           "(:medicoId IS NULL OR t.medico.id = :medicoId) AND " +
           "(:pacienteId IS NULL OR t.paciente.id = :pacienteId) AND " +
           "(:estado IS NULL OR t.estado = :estado) AND " +
           "(:fechaDesde IS NULL OR t.fechaHora >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR t.fechaHora <= :fechaHasta)")
    Page<Turno> findByFilters(
        @Param("medicoId") Long medicoId,
        @Param("pacienteId") Long pacienteId,
        @Param("estado") EstadoTurno estado,
        @Param("fechaDesde") Instant fechaDesde,
        @Param("fechaHasta") Instant fechaHasta,
        Pageable pageable
    );

    long countByEstado(EstadoTurno estado);
}
