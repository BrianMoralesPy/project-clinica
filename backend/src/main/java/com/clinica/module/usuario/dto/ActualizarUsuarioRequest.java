package com.clinica.module.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ActualizarUsuarioRequest(
    @Email(message = "El email debe ser válido")
    String email,

    String nombre,

    String apellido
) {}
