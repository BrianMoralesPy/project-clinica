package com.clinica.module.usuario.repository;

import com.clinica.module.usuario.entity.Usuario;
import com.clinica.shared.EstadoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:estado IS NULL OR u.estado = :estado)")
    Page<Usuario> findBySearchAndEstado(
        @Param("search") String search,
        @Param("estado") EstadoUsuario estado,
        Pageable pageable
    );
}
