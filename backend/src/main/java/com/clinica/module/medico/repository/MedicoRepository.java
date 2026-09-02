package com.clinica.module.medico.repository;

import com.clinica.module.medico.entity.Medico;
import com.clinica.shared.EstadoUsuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio encargado del acceso a datos de la entidad Medico.
 *
 * Extiende JpaRepository, por lo que hereda automáticamente las operaciones
 * CRUD básicas (guardar, buscar, actualizar, eliminar, etc.).
 *
 * Además, se definen métodos derivados y una consulta personalizada para
 * resolver búsquedas específicas de médicos.
 */
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    /**
     * Verifica si existe un médico con el DNI indicado.
     *
     * Se utiliza principalmente durante la creación de un médico para evitar
     * registrar dos fichas médicas con el mismo DNI.
     */
    boolean existsByDni(String dni);

    /**
     * Verifica si existe otro médico con el DNI indicado, excluyendo al médico
     * cuyo ID se recibe como parámetro.
     *
     * Se utiliza durante una actualización para permitir que el médico
     * conserve su propio DNI, pero impedir que lo utilice otro médico.
     */
    boolean existsByDniAndIdNot(String dni, Long id);

    /**
     * Verifica si existe otro médico con la matrícula indicada, excluyendo
     * al médico cuyo ID se recibe como parámetro.
     *
     * Se utiliza durante una actualización para evitar duplicar la matrícula
     * profesional entre distintos médicos.
     */
    boolean existsByMatriculaAndIdNot(String matricula, Long id);

    /**
     * Verifica si existe un médico con la matrícula profesional indicada.
     *
     * Se utiliza principalmente durante la creación de un médico para
     * garantizar que la matrícula sea única.
     */
    boolean existsByMatricula(String matricula);

    /**
     * Busca la ficha de médico asociada a un usuario determinado.
     *
     * La relación se realiza mediante el ID del usuario asociado al médico.
     *
     * Optional permite representar el caso en el que el usuario existe,
     * pero no tiene una ficha de médico asociada.
     */
    Optional<Medico> findByUsuarioId(Long usuarioId);

    /**
     * Obtiene médicos aplicando filtros opcionales por especialidad y estado.
     *
     * Si especialidadId es null, no se aplica el filtro de especialidad.
     * Si estado es null, no se aplica el filtro de estado.
     *
     * El resultado se devuelve paginado mediante Pageable, evitando cargar
     * todos los médicos en memoria cuando existen muchos registros.
     */
    @Query("""
        SELECT m
        FROM Medico m
        WHERE (:especialidadId IS NULL OR m.especialidad.id = :especialidadId)
        AND (:estado IS NULL OR m.usuario.estado = :estado)
        """)
    Page<Medico> findByEspecialidadAndEstado(
        @Param("especialidadId") Long especialidadId,
        @Param("estado") EstadoUsuario estado,
        Pageable pageable
    );
}