package com.clinica.module.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO utilizado para recibir las credenciales
 * de inicio de sesión.
 *
 * Spring validará automáticamente los campos
 * gracias a las anotaciones Bean Validation.
 */
public record LoginRequest(

    /**
     * Nombre de usuario o email.
     */
    @NotBlank(message = "El username o email es obligatorio")
    String identifier,

    /**
     * Contraseña del usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    String password

) {}