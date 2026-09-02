package com.clinica.module.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios
 * para crear un nuevo rol.
 *
 * Los datos recibidos son validados antes de ser
 * procesados por la aplicación.
 */
public record CrearRolRequest(

    /**
     * Nombre del rol.
     * Es obligatorio, no puede estar vacío y admite
     * un máximo de 50 caracteres.
     */
    @NotBlank
    @Size(max = 50)
    String nombre,

    /**
     * Descripción opcional del rol.
     * Si se proporciona, no puede superar los
     * 200 caracteres.
     */
    @Size(max = 200)
    String descripcion

) {}