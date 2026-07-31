package com.clinica.module.auth.dto;

import java.time.Instant;
import java.util.Set;

/**
 * Respuesta enviada al frontend luego de un
 * registro o un inicio de sesión exitoso.
 *
 * Contiene:
 * - El JWT generado.
 * - El tipo de token.
 * - Su tiempo de expiración.
 * - La información básica del usuario autenticado.
 */
public record AuthResponse(

    // Token JWT que deberá enviarse en el encabezado Authorization.
    String token,

    // Tipo de autenticación utilizado (normalmente "Bearer").
    String tipo,

    // Tiempo de validez del token en segundos.
    long expiresIn,

    // Información del usuario autenticado.
    UsuarioResponse usuario

) {

    /**
     * Información pública del usuario autenticado.
     *
     * No incluye información sensible como la contraseña.
     */
    public record UsuarioResponse(

        // Identificador único del usuario.
        Long id,

        // Nombre de usuario utilizado para iniciar sesión.
        String username,

        // Correo electrónico del usuario.
        String email,

        // Nombre.
        String nombre,

        // Apellido.
        String apellido,

        // Roles asignados al usuario.
        Set<String> roles,

        // Fecha de creación de la cuenta.
        Instant createdAt

    ) {}
}