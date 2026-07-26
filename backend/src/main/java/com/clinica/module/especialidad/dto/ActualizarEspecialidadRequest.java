package com.clinica.module.especialidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarEspecialidadRequest(
    @NotBlank
    @Size(max = 100)
    String nombre,

    @Size(max = 300)
    String descripcion
) {}