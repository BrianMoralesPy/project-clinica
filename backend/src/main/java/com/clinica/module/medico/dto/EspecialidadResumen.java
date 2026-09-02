package com.clinica.module.medico.dto;
/// DTO utilizado para representar un resumen de una especialidad médica.
public record EspecialidadResumen(
    // Identificador de la especialidad
    Long id,
    // Nombre de la especialidad
    String nombre
) {}