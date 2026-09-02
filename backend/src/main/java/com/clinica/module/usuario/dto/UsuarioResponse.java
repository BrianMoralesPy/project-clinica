package com.clinica.module.usuario.dto;

import java.time.Instant;
import java.util.Set;

/**
 * DTO utilizado para representar la información de un usuario
 * que será devuelta como respuesta al cliente.
 *
 * Permite controlar qué datos de la entidad Usuario
 * se exponen hacia el exterior de la aplicación.
 */
public record UsuarioResponse(

    /**
     * Identificador único del usuario.
     */
    Long id,

    /**
     * Nombre de usuario utilizado para identificarse en el sistema.
     */
    String username,

    /**
     * Correo electrónico del usuario.
     */
    String email,

    /**
     * Nombre del usuario.
     */
    String nombre,

    /**
     * Apellido del usuario.
     */
    String apellido,

    /**
     * Conjunto de roles asignados al usuario.
     *
     * Se utiliza Set para evitar roles duplicados.
     */
    Set<String> roles,

    /**
     * Estado actual del usuario.
     *
     * Se devuelve como String para representar el estado
     * de forma sencilla en la respuesta.
     */
    String estado,

    /**
     * Fecha y hora en la que se creó el usuario.
     *
     * Instant representa un instante concreto en el tiempo
     * y resulta adecuado para manejar fechas y horas
     * en respuestas de una API.
     */
    Instant createdAt

) {}