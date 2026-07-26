package com.clinica.module.turno.dto;

import java.time.Instant;

public record TurnoResponse(
    Long id,
    ResumenPaciente paciente,
    ResumenMedico medico,
    Instant fechaHora,
    Integer duracionMinutos,
    String motivo,
    String observaciones,
    String estado,
    Instant createdAt
) {}