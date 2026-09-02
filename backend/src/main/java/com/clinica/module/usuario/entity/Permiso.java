package com.clinica.module.usuario.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un permiso dentro del sistema.
 * Un permiso define una acción específica que puede ser
 * asignada a uno o varios roles.
 */
@Entity
@Table(name = "permiso")
@Getter
@Setter
public class Permiso {

    /**
     * Identificador único del permiso.
     * Se genera automáticamente mediante la estrategia
     * de identidad de la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre único del permiso.
     * Es obligatorio y permite identificar la acción
     * que representa, por ejemplo: "USUARIO_CREAR".
     */
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Descripción opcional del permiso.
     * Permite explicar qué acción o funcionalidad representa.
     */
    @Column(length = 200)
    private String descripcion;
}