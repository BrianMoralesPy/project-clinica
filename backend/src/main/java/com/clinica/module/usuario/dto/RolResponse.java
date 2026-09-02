package com.clinica.module.usuario.dto;

/**
 * DTO utilizado para representar la información de un rol
 * que será devuelta como respuesta al cliente.
 *
 * Permite controlar qué datos de la entidad Rol
 * se exponen hacia el exterior de la aplicación.
 */
public record RolResponse(

    /**
     * Identificador único del rol.
     */
    Long id,

    /**
     * Nombre del rol.
     */
    String nombre,

    /**
     * Descripción del rol.
     */
    String descripcion

) {}