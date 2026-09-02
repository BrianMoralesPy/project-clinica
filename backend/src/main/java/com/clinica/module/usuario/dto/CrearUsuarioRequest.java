package com.clinica.module.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado para recibir los datos necesarios
 * para crear un nuevo usuario.
 *
 * Los datos recibidos son validados antes de ser
 * procesados por la aplicación.
 */
public record CrearUsuarioRequest(

    /**
     * Nombre de usuario.
     * Es obligatorio y no puede superar los 50 caracteres.
     */
    @NotBlank(message = "El username es obligatorio")
    @Size(max = 50, message = "El username no puede superar los 50 caracteres")
    String username,

    /**
     * Correo electrónico del usuario.
     * Es obligatorio, debe tener un formato válido
     * y no puede superar los 100 caracteres.
     */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede superar los 100 caracteres")
    String email,

    /**
     * Contraseña del usuario.
     * Es obligatoria y debe tener entre 6 y 100 caracteres.
     *
     * La contraseña recibida será posteriormente procesada
     * y almacenada de forma segura por la capa de servicio.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
        min = 6,
        max = 100,
        message = "La contraseña debe tener entre 6 y 100 caracteres"
    )
    String password,

    /**
     * Nombre del usuario.
     * Es obligatorio y no puede superar los 100 caracteres.
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    String nombre,

    /**
     * Apellido del usuario.
     * Es obligatorio y no puede superar los 100 caracteres.
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres")
    String apellido,

    /**
     * Rol que se asignará al usuario.
     * Es obligatorio y permite identificar el rol
     * que tendrá el usuario dentro del sistema.
     */
    @NotNull(message = "El rol es obligatorio")
    String rol

) {}