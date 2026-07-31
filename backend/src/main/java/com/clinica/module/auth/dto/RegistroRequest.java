package com.clinica.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para registrar un nuevo usuario.
 *
 * Contiene los datos mínimos necesarios para crear
 * una cuenta dentro del sistema.
 *
 * Las validaciones se ejecutan automáticamente
 * antes de que el controlador invoque al servicio.
 */
public record RegistroRequest(

    /**
     * Nombre de usuario único.
     */
    @NotBlank(message = "El username es obligatorio")
    @Size(
        min = 3,
        max = 50,
        message = "El username debe tener entre 3 y 50 caracteres"
    )
    String username,

    /**
     * Correo electrónico del usuario.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    String email,

    /**
     * Contraseña elegida por el usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = 8,
        message = "La contraseña debe tener al menos 8 caracteres"
    )
    String password,

    /**
     * Nombre del usuario.
     */
    @NotBlank(message = "El nombre es obligatorio")
    String nombre,

    /**
     * Apellido del usuario.
     */
    @NotBlank(message = "El apellido es obligatorio")
    String apellido

) {}