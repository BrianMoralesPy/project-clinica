package com.clinica.module.paciente.repository;

import com.clinica.module.paciente.entity.Paciente;
import com.clinica.shared.EstadoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    /**
     * Verifica si ya existe un paciente con el DNI indicado.
     *
     * Se utiliza al crear un nuevo paciente para evitar
     * registros duplicados.
     */
    boolean existsByDni(String dni);


    /**
     * Verifica si otro paciente (distinto del actual)
     * posee el mismo DNI.
     *
     * Se utiliza durante la actualización para permitir
     * conservar el mismo DNI, pero impedir duplicarlo
     * en otro paciente.
     */
    boolean existsByDniAndIdNot(String dni, Long id);


    /**
     * Obtiene la ficha del paciente asociada a un Usuario.
     *
     * Gracias a la relación @OneToOne + @MapsId,
     * cada Usuario con rol PACIENTE posee una única
     * ficha de Paciente.
     *
     * Es utilizado principalmente para obtener el
     * paciente autenticado (/api/pacientes/me).
     */
    Optional<Paciente> findByUsuarioId(Long usuarioId);


    /**
     * Busca pacientes aplicando búsqueda por texto.
     *
     * Permite buscar por:
     * - Nombre
     * - Apellido
     * - DNI
     *
     * Si el parámetro "search" es nulo o vacío,
     * devuelve todos los pacientes de forma paginada.
     *
     * La paginación es gestionada automáticamente
     * por Spring Data mediante Pageable.
     */
    @Query("""
        SELECT p
        FROM Paciente p
        WHERE (
            :search IS NULL OR :search = '' OR
            LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(p.usuario.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(p.dni) LIKE LOWER(CONCAT('%', :search, '%'))
        )
    """)
    Page<Paciente> findBySearchAndEstado(
            @Param("search") String search,
            @Param("estado") EstadoUsuario estado,
            Pageable pageable);
}