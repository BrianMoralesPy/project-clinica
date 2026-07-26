package com.clinica.module.turno.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ActualizarTurnoRequest(
    @NotNull Instant fechaHora,
    Integer duracionMinutos
) {}