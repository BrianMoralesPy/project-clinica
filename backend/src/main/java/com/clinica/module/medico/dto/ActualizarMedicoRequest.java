package com.clinica.module.medico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarMedicoRequest(
    @NotBlank
    @Size(max = 100)
    String nombre,

    @NotBlank
    @Size(max = 100)
    String apellido,

    @NotNull
    Long especialidadId,

    @Size(max = 30)
    String telefono,

    @Size(max = 100)
    String email
) {}