package com.clinica.module.medico.dto;

import java.time.Instant;

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
    Instant createdAt
) {}