package com.clinica.module.medico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearMedicoRequest(
    @NotBlank @Size(max = 100) String nombre,
    @NotBlank @Size(max = 100) String apellido,
    @NotBlank @Size(max = 20) String dni,
    @NotBlank @Size(max = 50) String matricula,
    @NotNull Long especialidadId,
    @Size(max = 30) String telefono,
    @Size(max = 100) String email
) {}

public record ActualizarMedicoRequest(
    @NotBlank @Size(max = 100) String nombre,
    @NotBlank @Size(max = 100) String apellido,
    @NotNull Long especialidadId,
    @Size(max = 30) String telefono,
    @Size(max = 100) String email
) {}

public record MedicoResponse(
    Long id,
    String nombre,
    String apellido,
    String dni,
    String matricula,
    EspecialidadResumen especialidad,
    String telefono,
    String email,
    String estado,
    java.time.Instant createdAt
) {}

public record EspecialidadResumen(
    Long id,
    String nombre
) {}
