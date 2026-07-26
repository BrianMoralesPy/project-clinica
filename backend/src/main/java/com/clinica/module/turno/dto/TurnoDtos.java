package com.clinica.module.turno.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CrearTurnoRequest(
    @NotNull Long pacienteId,
    @NotNull Long medicoId,
    @NotNull Instant fechaHora,
    Integer duracionMinutos,
    String motivo
) {}

public record ActualizarTurnoRequest(
    @NotNull Instant fechaHora,
    Integer duracionMinutos
) {}

public record CompletarTurnoRequest(
    String observaciones
) {}

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

public record ResumenPaciente(
    Long id,
    String nombre,
    String apellido
) {}

public record ResumenMedico(
    Long id,
    String nombre,
    String apellido,
    String especialidad
) {}
