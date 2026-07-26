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