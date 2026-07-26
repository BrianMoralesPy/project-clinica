package com.clinica.module.especialidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearEspecialidadRequest(
    @NotBlank
    @Size(max = 100)
    String nombre,

    @Size(max = 300)
    String descripcion
) {}