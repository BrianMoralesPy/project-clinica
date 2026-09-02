package com.clinica.module.usuario.repository;

import com.clinica.module.usuario.entity.Usuario;
import com.clinica.shared.EstadoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/**
 * Repositorio encargado del acceso a datos de la entidad Usuario.
 *
 * Extiende JpaRepository, por lo que hereda automáticamente las
 * operaciones CRUD básicas sobre los usuarios.
 *
 * Además, define consultas específicas para buscar usuarios por
 * diferentes datos y para realizar búsquedas filtradas y paginadas.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    /**
     * Busca un usuario por su username.
     *
     * Optional permite representar el caso en el que no exista
     * ningún usuario con el username indicado.
     */
    Optional<Usuario> findByUsername(String username);


    /**
     * Busca un usuario por su email.
     *
     * Optional permite representar el caso en el que no exista
     * ningún usuario con el email indicado.
     */
    Optional<Usuario> findByEmail(String email);


    /**
     * Busca un usuario utilizando username o email.
     *
     * Es útil, por ejemplo, para permitir que el usuario pueda
     * iniciar sesión utilizando cualquiera de esos dos datos.
     */
    Optional<Usuario> findByUsernameOrEmail(
        String username,
        String email
    );


    /**
     * Verifica si existe un usuario con el username indicado.
     *
     * Se utiliza principalmente para validar que no se registren
     * usernames duplicados.
     */
    boolean existsByUsername(String username);


    /**
     * Verifica si existe un usuario con el email indicado.
     *
     * Se utiliza principalmente para validar que no se registren
     * emails duplicados.
     */
    boolean existsByEmail(String email);


    /**
     * Busca usuarios aplicando un filtro de texto y un filtro por estado.
     *
     * El parámetro search permite buscar coincidencias parciales
     * en nombre, apellido, username o email.
     *
     * El parámetro estado permite filtrar los usuarios por su estado,
     * por ejemplo ACTIVO o INACTIVO.
     *
     * Ambos filtros son opcionales. Si search es null o vacío,
     * no se aplica el filtro de texto. Si estado es null,
     * no se aplica el filtro de estado.
     *
     * El resultado se devuelve paginado mediante Pageable.
     */
    @Query("""
        SELECT u
        FROM Usuario u
        WHERE
            (:search IS NULL OR :search = '' OR
            LOWER(u.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(u.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:estado IS NULL OR u.estado = :estado)
        """)
    Page<Usuario> findBySearchAndEstado(
        @Param("search") String search,
        @Param("estado") EstadoUsuario estado,
        Pageable pageable
    );
}