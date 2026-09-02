package com.clinica.module.usuario.repository;

import com.clinica.module.usuario.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * Repositorio encargado del acceso a datos de la entidad Rol.
 *
 * Extiende JpaRepository, por lo que hereda automáticamente las
 * operaciones CRUD básicas sobre la tabla de roles.
 */
public interface RolRepository extends JpaRepository<Rol, Long> {


    /**
     * Busca un rol por su nombre.
     *
     * Optional permite representar el caso en el que no exista
     * ningún rol con el nombre indicado, evitando devolver null.
     */
    Optional<Rol> findByNombre(String nombre);


    /**
     * Verifica si ya existe un rol con el nombre indicado.
     *
     * Se utiliza principalmente antes de crear o modificar un rol
     * para evitar nombres duplicados.
     */
    boolean existsByNombre(String nombre);
}