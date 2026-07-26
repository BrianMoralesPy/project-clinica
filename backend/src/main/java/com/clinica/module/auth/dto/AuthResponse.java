package com.clinica.module.auth.dto;

import java.time.Instant;
import java.util.Set;

public record AuthResponse(
    String token,
    String tipo,
    long expiresIn,
    UsuarioResponse usuario
) {
    public record UsuarioResponse(
        Long id,
        String username,
        String email,
        String nombre,
        String apellido,
        Set<String> roles,
        Instant createdAt
    ) {}
}
