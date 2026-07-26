package com.clinica.module.dashboard.dto;

public record DashboardResponse(
    long totalPacientes,
    long totalMedicos,
    long totalEspecialidades,
    long turnosHoy,
    long turnosPendientes,
    long turnosCompletados
) {}
