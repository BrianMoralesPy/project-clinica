package com.clinica.module.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios
 * para actualizar un rol.
 *
 * Los datos recibidos son validados antes de ser
 * procesados por la aplicación.
 */
public record ActualizarRolRequest(

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
     * Si se proporciona, puede tener como máximo
     * 200 caracteres.
     */
    @Size(max = 200)
    String descripcion

) {}