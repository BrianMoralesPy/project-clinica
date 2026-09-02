package com.clinica.module.usuario.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un rol dentro del sistema.
 * Un rol agrupa un conjunto de permisos que determinan
 * las acciones que puede realizar un usuario.
 */
@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol {

    /**
     * Identificador único del rol.
     * Se genera automáticamente mediante la estrategia
     * de identidad de la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol.
     * Es obligatorio, no puede repetirse y admite hasta 50 caracteres.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    /**
     * Descripción opcional del rol.
     * Permite indicar de forma breve la finalidad o alcance del rol.
     */
    @Column(length = 200)
    private String descripcion;

    /**
     * Permisos asociados al rol.
     *
     * La relación es muchos a muchos, ya que:
     * - Un rol puede tener varios permisos.
     * - Un mismo permiso puede pertenecer a varios roles.
     *
     * Se utiliza FetchType.EAGER para cargar los permisos
     * junto con el rol.
     *
     * La tabla intermedia "rol_permiso" almacena la relación
     * entre roles y permisos.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rol_permiso",
        joinColumns = @JoinColumn(name = "rol_id"),
        inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private Set<Permiso> permisos = new HashSet<>();
}