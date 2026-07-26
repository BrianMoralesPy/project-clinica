package com.clinica.module.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarRolRequest(
    @NotBlank
    @Size(max = 50)
    String nombre,

    @Size(max = 200)
    String descripcion
) {}