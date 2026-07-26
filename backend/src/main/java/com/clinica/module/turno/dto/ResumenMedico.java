package com.clinica.module.turno.dto;

public record ResumenMedico(
    Long id,
    String nombre,
    String apellido,
    String especialidad
) {}